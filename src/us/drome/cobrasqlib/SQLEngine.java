package us.drome.cobrasqlib;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class allows for the standardized connection to a SQL database and the ability to send
 * synchronous or asynchronous queries to it directly via SQL or through the <tt>Table</tt> class.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public abstract class SQLEngine {
    protected final Logger logger;
    protected List<Table> tables;
    protected Connection connection;
    /**
     * ExecutorService is used to run queries asynchronously. It is instantiated
     * as a SingleThreadExecutor to queue all database operations so they are executed in order.
     */
    private final ExecutorService queryExecutor;
    
    protected SQLEngine (Logger logger) throws InvalidSQLConfigException {
        this.logger = logger;
        this.queryExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Runs a natively asynchronous query against this database that will return the
     * result of the query by executing a specified callback method via reflection.
     * 
     * The result of the query will be in the form of a <tt>List&lt;Map&lt;String, Object&gt;&gt;</tt> where each
     * <tt>List</tt> item is a row, and the <tt>Map</tt> contains the row contents as
     * key: column name, value: column contents.
     * 
     * @param query A string of the full SQL query to execute against this database.
     * @param callback A <tt>Callback</tt> object that contains the class reference and method that will
     * be executed once the query is finished. This method must accept a parameter of <tt>List&lt;Map&lt;String, Object&gt;&gt;</tt>.
     */
    public void runAsyncQuery (final String query, final Callback callback){
        queryExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.invoke(runQuery(query));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    /**
     * Runs a natively asynchronous update against this database.
     * 
     * @param update A string of the full SQL update statement to execute against this database.
     */
    public void runAsyncUpdate (final String update) {
        queryExecutor.execute(new Runnable() {
           @Override
           public void run() {
               runUpdate(update);
           }
        });
    }
    
    /**
     * Runs a synchronous query against this database.
     * 
     * The result of the query will be in the form of a <tt>List&lt;Map&lt;String, Object&gt;&gt;</tt> where each
     * <tt>List</tt> item is a row, and the <tt>Map</tt> contains the row contents as
     * key: column name, value: column contents.
     * 
     * @param query A string of the full SQL query to execute against this database.
     * @return a <tt>List&lt;Map&lt;String, Object&gt;&gt;</tt> representing the result set.
     */
    public List<Row> runQuery(String query) {
        Connection conn = getConnection();
        ResultSet result;
        ResultSetMetaData resultMeta;
        List<Row> resultList = new ArrayList<>();
        PreparedStatement statement;
        
        try {
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
            resultMeta = result.getMetaData();
            Table table = this.getTable(resultMeta.getTableName(1));
            while(result.next()) {
                Row row = new Row(this.getTable(resultMeta.getTableName(1)));
                for(int i = 1 ; i <= resultMeta.getColumnCount() ; i++) {
                    row.addColumn(new Column(row, table.getColumn(resultMeta.getColumnName(i)), result.getObject(i)));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            if(conn != null) {
                try {
                    logger.log(Level.SEVERE, e.getMessage() + " Attempting to roll back query.");
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        } finally {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { logger.log(Level.SEVERE, e.getMessage()); }
        }
        return resultList;
    }
    
        
    /**
     * Runs a synchronous update against this database.
     * 
     * @param update A string of the full SQL update statement to execute against this database.
     */
    public void runUpdate(String update) {
        Connection conn = getConnection();
        PreparedStatement statement;
        try {
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(update);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
             try {
                logger.log(Level.SEVERE, e.getMessage() + " Attempting to roll back update.");
                conn.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        } finally {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { logger.log(Level.SEVERE, e.getMessage()); }
        }
        
    }
 
    public abstract Connection getConnection();
    
    /**
     * Closes any open connections to the database.
     */
    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        } else {
            logger.log(Level.WARNING, "There are no connections to close.");
        }
    }
    
    /**
     * Properly shuts down this database connection including the query executor that could potentially hang the process it is running on.
     */
    public void shutdown() {
        if(queryExecutor != null) {
            queryExecutor.shutdown();
        }
        if(connection != null) {
            closeConnection();
        }
        logger.log(Level.INFO, "Database engine has been successfully shut down.");
    }
    
    /**
     * Protected method to retrieve the asynchronous executor.
     * @return The executor used to run asynchronous queries.
     */
    protected ExecutorService getExecutor() { return this.queryExecutor; }
    
    public abstract Table createTable(String name, ColumnDef... columns);
    
    public abstract Table getTable(String name);
    
    /**
     * Function to rename a table to the provided new name.
     * @param oldName The old table name.
     * @param newName The new table name.
     */
    public void renameTable(String oldName, String newName) {
        runAsyncUpdate("ALTER TABLE " + oldName + "RENAME TO " + newName);
        for(Table table : tables) {
            if(table.getName().equalsIgnoreCase(oldName)) {
                table.rename(newName);
            }
        }
    }
    
    /**
     * Function to remove a table from the database.
     * @param name The name of the table to remove.
     */
    public void dropTable(String name) { 
        runAsyncUpdate("DROP TABLE " + name);
        Iterator<Table>  tableIt = tables.iterator();
        while(tableIt.hasNext()) {
            if(tableIt.next().getName().equalsIgnoreCase(name)) {
                tableIt.remove();
            }
        }
    }
}
