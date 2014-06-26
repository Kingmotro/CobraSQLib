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
import java.sql.SQLException;
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
    
    @Override
    public void runQuery(final String query, final Method callback) throws SQLException {
        queryExecutor.execute(new Runnable() {
            public void run() {
                Connection conn = getConnection();
                ResultSet result = null;
                PreparedStatement statement = null;
                try {
                    statement = conn.prepareStatement(query);
                    result = statement.executeQuery();
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
                    callback.invoke(result);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(SQLiteEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    @Override
    public void runUpdate(String query) throws SQLException {
        
    }
    
    @Override
    public void runUpdate(String query, Method callback) throws SQLException {
        
    }
}
