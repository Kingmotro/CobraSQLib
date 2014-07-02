package us.drome.cobrasqlib;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

public abstract class SQLEngine {
    protected Logger logger;
    
    protected SQLEngine (Logger logger) throws InvalidSQLConfigException {
        this.logger = logger;
    }

    public abstract void runAsyncQuery (String query, Method callback) throws SQLException;
    
    public abstract void runAsyncUpdate (String update) throws SQLException;
    
    public abstract Map<String,Object> runSyncQuery(String query) throws SQLException;
    
    public abstract void runSyncUpdate(String update) throws SQLException;
    
    public abstract Connection getConnection();
    
    public abstract void closeConnection();
    
    public abstract void shutdown();
    
    public abstract Table getTable();
}
