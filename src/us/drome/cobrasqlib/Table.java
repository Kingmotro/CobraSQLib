package us.drome.cobrasqlib;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;

public abstract class Table {
    private SQLEngine parent;
    private final String name;
    private final Set<ColumnDef> columns;
    
    
    protected Table(SQLEngine parent, String name, ColumnDef... columns) {
        this.parent = parent;
        this.name = name;
        this.columns = (Set<ColumnDef>) Arrays.asList(columns);
    }
    
    public String getName() { return name; }
    
    public Set<ColumnDef> getColumns() { return columns; }
    
    public ColumnDef getColumn(String name) { 
        for(ColumnDef def : columns) {
            if(def.name.equalsIgnoreCase(name)) {
                return def;
            }
        }
        return null;
    }
    
    public abstract void addColumn(ColumnDef definition);

    public abstract void modifyColumn(String name, ColumnDef newDefinition);
    
    public void removeColumn(String name) { parent.runAsyncUpdate("ALTER TABLE " + this.name + " DROP COLUMN " + name); }
    
    public abstract void getRow(Object key, Callback callback) throws SQLException;
    
    public abstract void get(Object key, String column, Callback callback) throws SQLException;
    
    public abstract void setRow(Row row) throws SQLException;
    
    public abstract void set(Object key, String column, Object value) throws SQLException;
    
    public abstract void deleteRow(Row row) throws SQLException;
    
    public abstract void delete(Object key) throws SQLException;
}