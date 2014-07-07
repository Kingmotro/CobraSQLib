package us.drome.cobrasqlib;

/**
 * This class provides the specification of SQLite database field types for use with <tt>Column</tt>s
 * 
 * @author TheAcademician
 * @Since 0.1
 */
public class SQLiteType implements DataType {
    /**
     * An <tt>enum</tt> containing all valid SQLite data types.
     */
    enum SQLiteTypes { TEXT, NUMERIC, INTEGER, REAL, NONE; }
    
    /**
     * <tt>String</tt> object containing the data type of this <tt>SQLiteType</tt> object.
     */
    private final String type;
    
    /**
     * A private constructor that is used to instantiate a new <tt>SQLiteType</tt> object to a given type.
     * 
     * @param type the data type for this new <tt>SQLite</tt> object
     */
    private SQLiteType(String type) { this.type = type; }
    
    /**
     * Static method that is used to construct a new <tt>SQLiteType</tt> of type TEXT.
     * 
     * @return a <tt>SQLiteType</tt> of type TEXT
     */
    public static SQLiteType TEXT() { return new SQLiteType(SQLiteTypes.TEXT.name()); }
    
    /**
     * Static method that is used to construct a new <tt>SQLiteType</tt> of type NUMERIC.
     * 
     * @return a <tt>SQLiteType</tt> of type NUMERIC
     */
    public static SQLiteType NUMERIC() { return new SQLiteType(SQLiteTypes.NUMERIC.name()); }
    
    /**
     * Static method that is used to construct a new <tt>SQLiteType</tt> of type INTEGER.
     * 
     * @return a <tt>SQLiteType</tt> of type INTEGER
     */
    public static SQLiteType INTEGER() { return new SQLiteType(SQLiteTypes.INTEGER.name()); }
    
    /**
     * Static method that is used to construct a new <tt>SQLiteType</tt> of type REAL.
     * 
     * @return a <tt>SQLiteType</tt> of type REAL
     */
    public static SQLiteType REAL() { return new SQLiteType(SQLiteTypes.REAL.name()); }
    
    /**
     * Static method that is used to construct a new <tt>SQLiteType</tt> of type NONE.
     * 
     * @return a <tt>SQLiteType</tt> of type NONE
     */
    public static SQLiteType NONE() { return new SQLiteType(SQLiteTypes.NONE.name()); }
    
    @Override
    public String getType() { return type; } 
    
    @Override
    public Boolean canAutoincrement() {
        if(type.equals(SQLiteTypes.INTEGER.name())) {
            return true;
        } else {
            return false;
        }
    }
}
