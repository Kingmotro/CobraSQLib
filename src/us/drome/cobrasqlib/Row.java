package us.drome.cobrasqlib;

import java.util.HashMap;
import java.util.Map;

public class Row {
    private final Table parent;
    private HashMap<String, Object> data;
    
    /**
     * Represents a row inside a database table.
     * 
     * @param parent The table that this row is contained in.
     */
    public Row(Table parent) {
        this.parent = parent;
        this.data = new HashMap<>();
    }
    
    /**
     * Returns the parent table that this row belongs to.
     * 
     * @return
     */
    public Table getTable() { return this.parent; }
    
    /**
     * Returns the entire contents of this row as a HashMap.
     * 
     * @return
     */
    public Map<String, Object> getData() { return this.data; }
    
    /**
     * Set the contents of the specified column to the provided object.
     * To commit this change to the database you will need to run the Table/'s
     * setRow() method with this Row as the parameter.
     * 
     * @param column The name of the column you wish to update.
     * @param data The data you wish to insert into the column.
     */
    public void updateColumn(String column, Object data) { getData().put(column, data); }
    
    /**
     * Returns the raw Object representing the contents of the specified column.
     * 
     * @param column
     * @return
     */
    public Object get(String column) { return getData().get(column); }
    
    /**
     * Returns a String representing the contents of the specified column.
     * 
     * @param column
     * @return
     */
    public String getAsString(String column) { return String.valueOf(getData().get(column)); }
    
    /**
     * Attempts to return an Integer representing the contents of the specified column.
     * 
     * @param column
     * @throws NumberFormatException
     * @return
     */
    public Integer getAsInt(String column) throws NumberFormatException { return Integer.parseInt(getAsString(column)); }
}
