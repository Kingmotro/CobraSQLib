package us.drome.cobrasqlib;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
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


public class SQLiteEngine extends SQLEngine {
    private String file;
    private Connection connection;
    private ExecutorService queryExecutor;
    
    public SQLiteEngine (Logger logger, String file) throws InvalidSQLConfigException {
        super(logger);
        if(file == null || file.isEmpty()) {
            throw new InvalidSQLConfigException("File parameter is required for the SQLite Engine.");
        } else {
            this.file = file;
        }
        
        queryExecutor = Executors.newCachedThreadPool();
    }
    
    @Override
    public void shutdown() {
        if(connection != null) {
            closeConnection();
        }
        if(queryExecutor != null) {
            queryExecutor.shutdown();
        }
        logger.log(Level.INFO, "SQLite Engine has been successfully shut down.");
    }
    
    public File getFile() {
        File db = new File(file);
        if(!db.isAbsolute()) {
            try {
                db = new File(URLDecoder.decode(SQLiteEngine.class.getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8"), db.getPath());
            } catch (UnsupportedEncodingException ex) {
                logger.log(Level.SEVERE, "Unsupported encoding detected");
            }
        }
        return db;
    }
    
    @Override
    public Connection getConnection() {
        try {
            if(connection == null || !connection.isValid(10)) {
                connection = openConnection(getFile());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public Connection openConnection(File db) throws SQLException {
        try {
            Class.forName(org.sqlite.JDBC.class.getName());
            return DriverManager.getConnection("jdbc:sqlite:" + db);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Cannot load SQLite. Check your installation and try again.");
        }
    }
    
    @Override
    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error attempting to close SQLite connection.");
            }
        } else {
            logger.log(Level.WARNING, "There is no SQLite connection to close.");
        }
    }
    
    /**
     * Returns the table that this connection is currently working in.
     * 
     * @return
     */
    @Override
    public Table getTable() {
        return new Table("derp");
    }
    
    /**
     * Runs a natively asynchronous query against this SQLite database that will return the
     * result of the query by executing a specified callback method via reflection.
     * 
     * The result of the query will be in the form of a List<Map<String, Object>>containing
     * the key of column name and a value of the column contents.
     * 
     * @param query A string of the full SQLite query to execute against this database.
     * @param callback A method that will be executed once the query is finished. This method
     * must accept a parameter of List<Map<String, Object>>.
     * @throws SQLException
     */
    @Override
    public void runAsyncQuery(final String query, final Method callback) throws SQLException {
        queryExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Connection conn = getConnection();
                ResultSet result;
                ResultSetMetaData resultMeta;
                List<Map<String, Object>> resultList = new ArrayList<>();
                PreparedStatement statement;
                try {
                    statement = conn.prepareStatement(query);
                    result = statement.executeQuery();
                    resultMeta = result.getMetaData();
                    while(result.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for(int i = 1 ; i <= resultMeta.getColumnCount() ; i++) {
                            row.put(resultMeta.getColumnName(i), result.getObject(i));
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
                }
                
                try {
                    callback.invoke(resultList);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    /**
     * Runs a natively asynchronous update against this SQLite database.
     * 
     * @param update A string of the full SQLite update statement to execute against this database.
     * @throws SQLException
     */
    @Override
    public void runAsyncUpdate(final String update) throws SQLException {
        queryExecutor.execute(new Runnable() {
           @Override
           public void run() {
               Connection conn = getConnection();
               PreparedStatement statement;
               try {
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
               }
           }
        });
    }
    
    /**
     * Runs a synchronous query against this SQLite database.
     * 
     * The result of the query will be in the form of a List<Map<String, Object>> containing
     * the key of column name and a value of the column contents.
     * 
     * @param query A string of the full SQLite query to execute against this database.
     * @return Returns a List<Map<String, Object>> representing the result set.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> runSyncQuery(String query) throws SQLException {
        return new HashMap<>();
    }
    
    /**
     * Runs a synchronous update against this SQLite database.
     * 
     * @param update A string of the full SQLite update statement to execute against this database.
     * @throws SQLException
     */
    @Override
    public void runSyncUpdate(String update) throws SQLException {
        
    }
}
