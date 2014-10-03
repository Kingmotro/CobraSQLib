package us.drome.cobrasqlib;

import java.sql.SQLException;

/**
 * The <tt>Column</tt> class represents a single column in a <tt>Row</tt> object. It contains both the column's definition
 * and data which can be modified and pushed to the database.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class Column {
    private final Row parent;
    private final ColumnDef definition;
    private Object data;
    
    /**
     * Protected constructor to initialize a new <tt>Column</tt> instance.
     * @param parent A reference to the parent row of this column.
     * @param definition The definition of the column.
     * @param data The data contained inside the column.
     */
    protected Column(Row parent, ColumnDef definition, Object data) {
        this.parent = parent;
        this.definition = definition;
        this.data = data;
    }
    
    /**
     * Function to return a reference to the Table this data is from.
     * @return the parent <tt>Table</tt> of the <tt>Row</tt> this <tt>Column</tt> is in.
     */
    public Table getTable() { return parent.getTable(); }
    
    /**
     * Function to return a reference to the parent <tt>Row</tt>.
     * @return the parent <tt>Row</tt>.
     */
    public Row getRow() { return parent; }
    
    /**
     * @return The column's name.
     */
    public String getName() { return definition.name; }
    
    /**
     * @return The column's data type.
     */
    public Type getType() { return definition.type; }
    
    /**
     * This function returns the class that this data type can be cast to on retrieval.
     * @return The column's Java class return type.
     */
    public Class getReturnType() { return definition.getReturnType(); }
    
    /**
     * @return true if this column is the primary key.
     */
    public boolean isPrimaryKey() { return definition.isPrimary; }
    
    /**
     * @return true if the column does not contain null.
     */
    public boolean isNotNull() { return (data == null ? true : false); }
    
    /**
     * @return The data contained in the column as an <tt>Object</tt>.
     */
    public Object getData() { return (data); }
    
    /**
     * Set the data in this column to the provided <tt>Object</tt>.
     * @param data The column's new data.
     */
    public void setData(Object data) { this.data = data; }
    
    /**
     * Executes an update against the table with this column.
     * @throws SQLException
     */
    public void updateColumn() throws SQLException { parent.updateRow(); }
}