package us.drome.cobrasqlib;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * The <tt>Row</tt> class represents a row of any database table as an object. It contains the columns and their values
 * as well as a reference to the <tt>Table</tt> that the row belongs.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class Row {
    private final Set<Column> columns;
    private final Table parent;
    
    /**
     * A protected constructor to initialize a new Row instance.
     * 
     * @param parent The <tt>Table</tt> object that this row was queried from.
     * @param columns The individual table columns which contain the data.
     */
    protected Row(Table parent, Column... columns) {
        this.parent = parent;
        this.columns = (Set<Column>) Arrays.asList(columns);
    }
    
    /**
     * Protected method to add a column with data to an existing row.
     * @param column The column with data to add.
     */
    protected void addColumn(Column column) { columns.add(column); }
    
    /**
     * Protected method to remove a column and it's data from the row.
     * @param column The column to remove.
     */
    protected void removeColumn(Column column) { columns.remove(column); }
    
    /**
     * Retrieve the parent table for this row.
     * @return <tt>Table</tt> parent object.
     */
    public Table getTable() { return parent; }
    
    /**
     * Function to retrieve a column by name.
     * @param name The column's name.
     * @return The <tt>Column</tt> object containing the data.
     */
    public Column getColumn(String name) {
        for(Column column : columns) {
            if(column.getName().equalsIgnoreCase(name)) {
                return column;
            }
        }
        return null;
    }
    
    /**
     * Executes an update for this row on the parent table.
     * 
     * @throws SQLException
     */
    public void updateRow() throws SQLException { parent.updateRow(this); }
    
    /**
     * Removes this row from the parent table.
     * 
     * @throws SQLException
     */
    public void deleteRow() throws SQLException { parent.deleteRow(this); }
    
    /**
     * @return true if this row contains no columns.
     */
    public boolean isEmpty() { return columns.isEmpty(); }
    
    /**
     * @return The amount of columns present in this row.
     */
    public int getSize() { return columns.size(); }
    
    /**
     * Function to determine if the specific column exists.
     * @param name The column to find by name.
     * @return true if the column exists.
     */
    public boolean contains(String name) {
        for(Column column : columns) {
            if(column.getName().equals(name)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Function to determine if all specified column names exist in this row.
     * @param names A array of column names to check for.
     * @return true if all the columns are found.
     */
    public boolean containsAll(String... names) {
        for(String name : names) {
            for(Column column : columns) {
                if(column.getName().equals(name)) {
                    break;
                }
            }
            return false;
        }
        return true;
    }
    
    /**
     * @return true if this row contains a primary key column.
     */
    public boolean hasPrimaryKey() {
        for(Column column: columns) {
            if(column.isPrimaryKey()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return The <tt>Iterator</tt> for the contained columns.
     */
    public Iterator<Column> Iterator() { return columns.iterator(); }
    
    /**
     * @return An array of all the columns in this row.
     */
    public Column[] toArray() { return columns.toArray(new Column[columns.size()]); }
    
    /**
     * @param row Another <tt>Row</tt> object to compare to.
     * @return true if the rows are identical.
     */
    public boolean equals(Row row) { return columns.equals(row); }
    
}