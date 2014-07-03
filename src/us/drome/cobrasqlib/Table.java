package us.drome.cobrasqlib;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

public abstract class Table {
    protected String name;
    
    protected Table(String name) {
        this.name = name;
    }
    
    public abstract String getName();
    
    public abstract ColumnSet getColumns();
    
    public abstract Column getColumn(String name);
    
    public abstract void getRow(Object key, Method callback) throws SQLException;
    
    public abstract void get(Object key, String column, Method callback) throws SQLException;
    
    public abstract void setRow(Row row) throws SQLException;
    
    public abstract void set(Object key, String column, Object value) throws SQLException;
    
    public abstract void deleteRow(Row row) throws SQLException;
    
    public abstract void delete(Object key) throws SQLException;
}
