package us.drome.cobrasqlib;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public abstract class SQLEngine {
    protected Logger logger;
    
    protected SQLEngine (Logger logger) throws InvalidSQLConfigException {
        this.logger = logger;
    }

    public abstract void runQuery (String query, Method callback) throws SQLException;

    public abstract void runUpdate (String update) throws SQLException;
    
    public abstract void runUpdate (String update, Method callback) throws SQLException;
    
    public abstract Connection getConnection();
    
    public abstract void closeConnection();
    
    public abstract void shutdown();
}
