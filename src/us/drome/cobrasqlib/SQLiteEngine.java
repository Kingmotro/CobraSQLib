package us.drome.cobrasqlib;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class represents a connection to a SQLite database file.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class SQLiteEngine extends SQLEngine {
    private String file;
    Connection connection;
    
    /**
     * Construct a new <tt>SQLiteEngine</tt> by specifying a logger for output and a path to database file.
     * 
     * @param logger a <tt>Logger</tt> instance for sending output.
     * @param file a <tt>String</tt> containing the path to the database file.
     * @throws InvalidSQLConfigException
     */
    public SQLiteEngine (Logger logger, String file) throws InvalidSQLConfigException {
        super(logger);
        if(file == null || file.isEmpty()) {
            throw new InvalidSQLConfigException("File parameter is required for the SQLite Engine.");
        } else {
            this.file = file;
        }
    }
    
    /**
     * Retrieve the <tt>File</tt> instance containing the location to the SQLite database file.
     * 
     * @return a <tt>File</tt> object containing the location to the database file.
     */
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
    
    private Connection openConnection(File db) throws SQLException {
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
    public void shutdown() {
        super.shutdown();
        if(connection != null) {
            closeConnection();
        }
        logger.log(Level.INFO, "SQLite Engine has been successfully shut down.");
    }
    
    /**
     * Returns the specified table if it is in the database.
     * 
     * @param name The name of the <tt>Table</tt> to return.
     * @return
     */
    @Override
    public SQLiteTable getTable(String name) {
        return new SQLiteTable(name);
    }
}
