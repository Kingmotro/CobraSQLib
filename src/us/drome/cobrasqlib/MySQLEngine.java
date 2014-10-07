package us.drome.cobrasqlib;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a connection to a MySQL database.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class MySQLEngine extends SQLEngine {
    private final String url;
    private final String username;
    private final String password;
    
    /**
     * Construct a new <tt>MySQLEngine</tt> by specifying a Logger for output and the necessary connection data.
     * @param logger The output provider for this engine.
     * @param hostname The server hosting the database.
     * @param port The port the server is using.
     * @param database The name of the database instance.
     * @param username The username with access to this database.
     * @param password The password for the provided username.
     * @throws InvalidSQLConfigException
     */
    public MySQLEngine(Logger logger, String hostname, int port, String database, String username, String password) throws InvalidSQLConfigException {
        super(logger);
        this.url = hostname + ":" + String.valueOf(port) + "/" + database;
        this.username = username;
        this.password = password;
    }

    /**
     * @return A <tt>Connection</tt> object to provide connectivity with the database.
     */
    @Override
    public Connection getConnection() {
        try {
            if(connection == null || connection.isValid(10)) {
                return openConnection();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        
        return connection;
    }
    
    private Connection openConnection() throws SQLException {
        try {
            Class.forName(com.mysql.jdbc.Driver.class.getName());
            return DriverManager.getConnection("jdbc:mysql://" + url, username, password);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Cannot load MySQL. Check your installation and try again.");
        }
    }

    /**
     * Function to create a new table for this database.
     * @param name The name of the table to create.
     * @param columns An array of column definitions to construct the table.
     * @return The newly created table object.
     */
    @Override
    public Table createTable(String name, ColumnDef... columns) {
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
            tempDef += def.isAutoincrement ? " AUTO_INCREMENT" : "";
            if(i == columns.length -1) {
                tempDef += ",";
            }
            definitions += tempDef;
        }
        this.runAsyncUpdate("CREATE TABLE " + name + "(" + definitions + ")");
        MySQLTable table = new MySQLTable(this, name, columns);
        tables.add(table);
        return table;
    }

    /**
     * Returns the specified table if it is in the database.
     * 
     * @param name The name of the <tt>Table</tt> to return.
     * @return <tt>Table</tt> instance matching the provided name, or <tt>null</tt> if table does not exist.
     */
    @Override
    public MySQLTable getTable(String name) {
        for(Table table : tables) {
            if(table.getName().equalsIgnoreCase(name)) {
                return (MySQLTable)table;
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
                    MySQLTable thisTable = new MySQLTable(this, table, definitions);
                    return thisTable;
                }
            }
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        
        return null;
    }
}
