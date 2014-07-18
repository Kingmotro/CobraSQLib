package us.drome.cobrasqlib;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public abstract class newSQLEngine {
    protected final Logger logger;
    private Table[] tables;
    /**
     * ExecutorService is used to run queries asynchronously. It is instantiated
     * as a SingleThreadExecutor to queue all database operations so they are executed in order.
     */
    private final ExecutorService queryExecutor;
    
    protected newSQLEngine (Logger logger) throws InvalidSQLConfigException {
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
            e.printStackTrace();
            if(conn != null) {
                try {
                    logger.log(Level.SEVERE, "Attempting to roll back query.");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { logger.log(Level.WARNING, "Unable to set AutoCommit to true."); }
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
            e.printStackTrace();
             try {
                logger.log(Level.SEVERE, "Attempting to roll back update.");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { logger.log(Level.WARNING, "Unable to set AutoCommit to true."); }
        }
    }
    
    /**
     * Retrieve or instantiate a <tt>Connection</tt> to this database.
     * 
     * @return a <tt>>Connection</tt> object allowing connectivity with this database.
     */
    public abstract Connection getConnection();
    
    public abstract void closeConnection();
    
    /**
     * Properly shuts down this database connection including the query executor that could potentially hang the process it is running on.
     */
    public void shutdown() {
        if(queryExecutor != null) {
            queryExecutor.shutdown();
        }
    }
    
    public abstract Table createTable(TableBuilder builder);
    
    /**
     * Retrieves a <tt>Table</tt> instance for the specified table name from this database.
     * @param name The name of the database table you wish to retrieve.
     * @return a <tt>Table</tt> object for the specified table.
     */
    public abstract Table getTable(String name);
    
    public abstract void renameTable(String oldName, String newName);
    
    public abstract void dropTable(String name);
}