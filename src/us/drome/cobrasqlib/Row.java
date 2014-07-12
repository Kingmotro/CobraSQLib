package us.drome.cobrasqlib;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class Row {
    private final Set<Column> columns;
    private final Table parent;
    
    protected Row(Table parent, Column... columns) {
        this.parent = parent;
        this.columns = (Set<Column>) Arrays.asList(columns);
    }
    
    protected void addColumn(Column column) { columns.add(column); }
    
    protected void removeColumn(Column column) { columns.remove(column); }
    
    public Table getTable() { return parent; }
    
    public Column getColumn(String name) {
        for(Column column : columns) {
            if(column.getName().equalsIgnoreCase(name)) {
                return column;
            }
        }
        return null;
    }
    
    public void updateRow() throws SQLException { parent.setRow(this); }
    
    public void deleteRow() throws SQLException { parent.deleteRow(this); }
    
    public boolean isEmpty() { return columns.isEmpty(); }
    
    public int getSize() { return columns.size(); }
    
    public boolean contains(Column column) { return columns.contains(column); }
    
    public boolean containsAll(Column... columns) { return this.columns.containsAll(Arrays.asList(columns)); }
    
    public boolean hasPrimaryKey() {
        for(Column column: columns) {
            if(column.isPrimaryKey()) {
                return true;
            }
        }
        return false;
    }
    
    public Iterator<Column> Iterator() { return columns.iterator(); }
    
    public Column[] toArray() { return columns.toArray(new Column[columns.size()]); }
    
    public boolean equals(Row row) { return columns.equals(row); }
    
}