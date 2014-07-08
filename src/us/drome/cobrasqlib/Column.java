package us.drome.cobrasqlib;

import java.sql.Types;

/**
 * This class represents a <tt>Column</tt> definition for a <tt>Table</tt> in the database of any engine.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class Column {
    private final String name;
    private final Types type;
    private final int precision;
    private final int scale;
    private final boolean isPrimary;
    private final boolean isAutoincrement;
    private final boolean isNullable;
    private final Table parent;
    
    public Column(String name, Types type, Table parent) {
        this.name = name;
        this.type = type;
        this.parent = parent;
        precision = 0;
        scale = 0;
    }
    
    public Column(String name, Types type, int precision, int scale, boolean isPrimary, boolean isAutoincrement, boolean isNullable, Table parent) {
        
    }
    
    /**
     * Constructs a new <tt>Column</tt> with the provided name, {@link DataType}, a <tt>boolean</tt>
     * indicating if this is the primary key, and a boolean indicating if this <tt>Column</tt> should auto-increment.
     * 
     * @param name The name of the <tt>Column</tt>
     * @param dataType The allowed input type for this <tt>Column</tt>
     * @param isPrimary Indicates if this is the primary key
     * @param isAutoincrement Sets this <tt>Column</tt> to automatically increment for each row
     * @throws IllegalArgumentException if column is not INTEGER type and PRIMARY KEY
     */
    public Column(String name, DataType dataType, boolean isPrimary, boolean isAutoincrement) {
        this.name = name;
        this.dataType = dataType;
        this.isPrimary = isPrimary;
        if(isAutoincrement && dataType.canAutoincrement() && isPrimary) {
            this.isAutoincrement = isAutoincrement;
        } else {
            throw new IllegalArgumentException("This Column does not meet SQLite requirements for AUTOINCREMENT; it must be INTEGER type and PRIMARY KEY.");
        }
    }
    
    /**
     * Constructs a new <tt>Column</tt> with the provided name, {@link DataType}, and a <tt>boolean</tt>
     * indicating if this is the primary key for the table.
     * 
     * @param name The name of the <tt>Column</tt>
     * @param dataType The allowed input type for this <tt>Column</tt>
     * @param isPrimary Indicates if this is the primary key
     */
    public Column(String name, DataType dataType, boolean isPrimary) {
        this.name = name;
        this.dataType = dataType;
        this.isPrimary = isPrimary;
        this.isAutoincrement = false;
    }
    
    /**
     * Returns the name of this <tt>Column</tt>.
     * 
     * @return <tt>Column</tt> name as String
     */
    public String getName() { return this.name; }
    
    /**
     * Returns the {@link DataType} for this <tt>Column</tt> that represents it's accepted input.
     * 
     * @return a <tt>DataType</tt> object
     */
    public DataType getType() { return this.dataType; }
    
    /**
     * Returns <tt>true</tt> if this <tt>Column</tt> is set to be a primary key.
     * 
     * @return <tt>true</tt> if this <tt>Column</tt> is a primary key.
     */
    public boolean isPrimary() { return this.isPrimary; }
}
