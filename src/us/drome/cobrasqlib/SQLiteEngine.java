package us.drome.cobrasqlib;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a connection to a SQLite database file.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class SQLiteEngine extends SQLEngine {
    private String file;
    
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
    
    /**
     * @return A <tt>Connection</tt> object to provide connectivity with the database.
     */
    @Override
    public Connection getConnection() {
        try {
            if(connection == null || !connection.isValid(10)) {
                connection = openConnection(getFile());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
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
    
    /**
     * Returns the specified table if it is in the database.
     * 
     * @param name The name of the <tt>Table</tt> to return.
     * @return <tt>Table</tt> instance matching the provided name, or <tt>null</tt> if table does not exist.
     */
    @Override
    public SQLiteTable getTable(String name) {
        for(Table table : tables) {
            if(table.getName().equalsIgnoreCase(name)) {
                return (SQLiteTable)table;
            }
        }
        
        try {
            String table = "";
            ColumnDef[] definitions;
            
            DatabaseMetaData dbMeta = getConnection().getMetaData();
            ResultSet rs = dbMeta.getTables(null, null, "%", null);
            while (rs.next()) {
                if(rs.getString(3).equalsIgnoreCase(name)) {
                    table = rs.getString(3);
                    definitions = ColumnDef.generateDefs(table, dbMeta);
                    SQLiteTable thisTable = new SQLiteTable(this, table, definitions);
                    return thisTable;
                }
            }
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        
        return null;
    }

    /**
     * Function to create a new table for this database.
     * @param name The name of the table to create.
     * @param columns An array of column definitions to construct the table.
     * @return The newly created table object.
     */
    @Override
    public SQLiteTable createTable(String name, ColumnDef... columns) {
        String definitions = "";
        for(int i = 0; i < columns.length; i++) {
            ColumnDef def = columns[i];
            String tempDef = def.name + " ";
            tempDef += def.type;
            if(def.size >= 0 || def.decimal >= 0) {
                tempDef += "(";
                tempDef += def.size >= 0 ? def.size : "";
                tempDef += def.decimal >= 0 ? "," + def.decimal : "";
                tempDef += ")";
            }
            tempDef += def.isUnsigned ? " UNSIGNED" : "";
            tempDef += def.isNotNull ? " NOT NULL" : "";
            tempDef += def.isPrimary ? " PRIMARY KEY" : "";
            tempDef += def.isAutoincrement ? " AUTOINCREMENT" : "";
            if(i == columns.length -1) {
                tempDef += ",";
            }
            definitions += tempDef;
        }
        this.runAsyncUpdate("CREATE TABLE " + name + "(" + definitions + ")");
        SQLiteTable table = new SQLiteTable(this, name, columns);
        tables.add(table);
        return table;
    }
}
