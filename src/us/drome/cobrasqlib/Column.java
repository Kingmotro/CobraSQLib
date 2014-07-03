package us.drome.cobrasqlib;

/**
 * This class represents a Column definition for a Table in the database of any engine.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class Column {
    private final String name;
    private final DataType dataType;
    private final boolean isPrimary;
    
    /**
     * Constructs a new column with the provided name, DataType, and a boolean
     * indicating if this is the primary key for the table.
     * 
     * @param name The name of the column
     * @param dataType The allowed input type for this column
     * @param isPrimary Indicates if this is the primary key
     */
    public Column(String name, DataType dataType, boolean isPrimary) {
        this.name = name;
        this.dataType = dataType;
        this.isPrimary = isPrimary;
    }
    
    /**
     * Returns the name of this column.
     * 
     * @return column name as String
     */
    public String getName() { return this.name; }
    
    /**
     * Returns the {@link DataType} for this column that represents it's accepted input.
     * 
     * @return a DataType object
     */
    public DataType getType() { return this.dataType; }
    
    /**
     * Returns <tt>true</tt> if this column is set to be a primary key.
     * 
     * @return <tt>true</tt> if this column is a primary key.
     */
    public boolean isPrimary() { return this.isPrimary; }
}
