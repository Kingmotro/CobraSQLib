package us.drome.cobrasqlib;

import java.sql.SQLException;

public class Column {
    private final Row parent;
    private final ColumnDef definition;
    private Object data;
    
    protected Column(Row parent, ColumnDef definition, Object data) {
        this.parent = parent;
        this.definition = definition;
        this.data = data;
    }
    
    public Table getTable() { return parent.getTable(); }
    
    public Row getRow() { return parent; }
    
    public String getName() { return definition.name; }
    
    public Type getType() { return definition.type; }
    
    public boolean isPrimaryKey() { return definition.isPrimary; }
    
    public boolean isNotNull() { return (data == null ? true : false); }
    
    public Object getData() { return (data); }
    
    public void setData(Object data) { this.data = data; }
    
    public void updateColumn() throws SQLException { parent.updateRow(); }
}