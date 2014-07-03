package us.drome.cobrasqlib;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a Row inside a database Table and includes methods to
 * retrieve and set data.
 * 
 * @author TheAcademician
 * @since 0.1
 */
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
     * @return a <tt>Table</tt> object that is the parent to this <tt>Row</tt> object.
     */
    public Table getTable() { return this.parent; }
    
    /**
     * Returns the entire contents of this row as a Map.
     * 
     * @return a <tt>Map</tt> representing this <tt>Row</tt>'s data.
     */
    public Map<String, Object> getData() { return this.data; }
    
    /**
     * Set the contents of the specified column to the provided object.
     * To commit this change to the database you will need to run the Table's
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
     * @return <tt>Object</tt> representing the contents of the specific column.
     */
    public Object get(String column) { return getData().get(column); }
    
    /**
     * Returns a String representing the contents of the specified column.
     * 
     * @param column
     * @return <tt>OString</tt> representing the contents of the specific column.
     */
    public String getAsString(String column) { return String.valueOf(getData().get(column)); }
    
    /**
     * Attempts to return an Integer representing the contents of the specified column.
     * 
     * @param column
     * @throws NumberFormatException
     * @return <tt>Integer</tt> representing the contents of the specific column.
     */
    public Integer getAsInt(String column) throws NumberFormatException { return Integer.parseInt(getAsString(column)); }
}
