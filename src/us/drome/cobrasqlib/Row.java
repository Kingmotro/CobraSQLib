package us.drome.cobrasqlib;

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
    private Map<String, Object> data;
    
    /**
     * Represents a <tt>Row</tt> inside a database <tt>Table</tt>.
     * 
     * @param parent The <tt>Table</tt> that this <tt>Row</tt> is contained in.
     * @param data A <tt>Map</tt> with key:column name & value:column data.
     */
    public Row(Table parent, Map<String, Object> data) {
        this.parent = parent;
        this.data = data;
    }
    
    /**
     * Returns the parent <tt>Table</tt> that this <tt>Row</tt> belongs to.
     * 
     * @return a <tt>Table</tt> object that is the parent to this <tt>Row</tt> object.
     */
    public Table getTable() { return this.parent; }
    
    /**
     * Returns the entire contents of this <tt>Row</tt> as a <tt>Map</tt>.
     * 
     * @return a <tt>Map</tt> representing this <tt>Row</tt>'s data.
     */
    public Map<String, Object> getData() { return this.data; }
    
    /**
     * Set the contents of the specified column to the provided <tt>Object</tt>.
     * To commit this change to the database you will need to run the <tt>Table</tt>'s
     * <<tt>setRow()</tt> method with this Row as the parameter.
     * 
     * @param column The name of the column you wish to update.
     * @param data The data you wish to insert into the column.
     */
    public void updateColumn(String column, Object data) { getData().put(column, data); }
    
    /**
     * Returns the raw <tt>Object</tt> representing the contents of the specified column.
     * 
     * @param column The name of the column you wish to retrieve data from.
     * @return <tt>Object</tt> representing the contents of the specific column.
     */
    public Object get(String column) { return getData().get(column); }
    
    /**
     * Returns a <tt>String</tt> representing the contents of the specified column.
     * 
     * @param column The name of the column you wish to retrieve data from.
     * @return <tt>OString</tt> representing the contents of the specific column.
     */
    public String getAsString(String column) { return String.valueOf(getData().get(column)); }
    
    /**
     * Attempts to return an <tt>Integer</tt> representing the contents of the specified column.
     * 
     * @param column The name of the column you wish to retrieve data from.
     * @throws NumberFormatException if column contents cannot be parsed as an integer.
     * @return <tt>Integer</tt> representing the contents of the specific column.
     */
    public Integer getAsInt(String column) throws NumberFormatException { return Integer.parseInt(getAsString(column)); }
}
