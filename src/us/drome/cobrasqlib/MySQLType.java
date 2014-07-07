package us.drome.cobrasqlib;

/**
 * This class provides the specification of MySQL database field types for use with <tt>Column</tt>s
 * 
 * @author TheAcademician
 * @Since 0.1
 */
public class MySQLType implements DataType {
    /**
     * an <tt>enum</tt> containing all valid MySQL data types.
     */
    enum MySQLTypes { CHAR, VARCHAR, TINYTEXT, TEXT, BLOB, BOOLEAN,
    TINYINT, SMALLINT, MEDIUMINT, INT, FLOAT, DOUBLE, DECIMAL,
    DATE, DATETIME, TIMESTAMP, TIME; }
    
    /**
    * <tt>String</tt> object containing the data type of this <tt>MySQLType</tt> object.
    */
    private final String type;
    /**
     * <tt>Integer</tt> object containing the field size of this data type object.
     */
    private final Integer size;
    /**
     * <tt>Integer</tt> object containing the amount of decimal places this data type object is set to allow.
     */
    private final Integer decimal;
    /**
     * <tt>Boolean</tt> object determining if this field is signed or unsigned.
     */
    private final Boolean signed;
    
    /**
     * Private constructor used to instantiate a new <tt>MySQLType</tt> by specifying name & size.
     * 
     * @param type Name of the data type that this <tt>MySQLType</tt> will represent.
     * @param size <tt>Integer</tt> object representing the size of this field.
     */
    private MySQLType(String type, Integer size) { this.type = type; this.size = size; this.decimal = null; this.signed = null; }
    
    /**
     * Private constructor used to instantiate a new <tt>MySQLType</tt> by specifying name, size & decimal places.
     * 
     * @param type Name of the data type that this <tt>MySQLType</tt> will represent.
     * @param size <tt>Integer</tt> object representing the size of this field.
     * @param decimal <tt?Integer</tt> object representing the amount of decimal places allowed.
     */
    private MySQLType(String type, Integer size, Integer decimal) { this.type = type; this.size = size; this.decimal = decimal; this.signed = null; }
    
    /**
     * Private constructor used to instantiate a new <tt>MySQLType</tt> by specifying name, size, & if this value is signed.
     * 
     * @param type Name of the data type that this <tt>MySQLType</tt> will represent.
     * @param size <tt>Integer</tt> object representing the size of this field.
     * @param signed <tt>Boolean</tt> object indicating if this value is signed or unsigned.
     */
    private MySQLType(String type, Integer size, Boolean signed) { this.type = type; this.size = size; this.decimal = null; this.signed = signed; }
    
    /**
     * Private constructor used to instantiate a new <tt>MySQLType</tt> by specifying name, size, decimal places, & if this value is signed.
     * 
     * @param type Name of the data type that this <tt>MySQLType</tt> will represent.
     * @param size <tt>Integer</tt> object representing the size of this field.
     * @param decimal <tt?Integer</tt> object representing the amount of decimal places allowed.
     * @param signed <tt>Boolean</tt> object indicating if this value is signed or unsigned.
     */
    private MySQLType(String type, Integer size, Integer decimal, Boolean signed) { this.type = type; this.size = size; this.decimal = decimal; this.signed = signed; }
    
    /**
     * Static initializer for <tt>MySQLType</tt> CHAR.
     * 
     * @param size The size of this CHAR field. Valid range: 1-255.
     * @return returns a new <tt>MySQLType</tt> of type CHAR.
     */
    public static MySQLType CHAR(Integer size) {
        if(size == null || (size < 1 || size > 255)) {
            throw new IllegalArgumentException("CHAR field must have a size between 1 and 255 characters.");
        }
        return new MySQLType(MySQLTypes.CHAR.name(), size);
    }
    
    /**
     * Static initializer for <tt>MySQLType</tt> VARCHAR.
     * 
     * @param size The size of this VARCHAR field. Valid range: 1-255.
     * @return returns a new <tt>MySQLType</tt> of type VARCHAR.
     */
    public static MySQLType VARCHAR(Integer size) {
        if(size == null || (size < 1 || size > 255)) {
            throw new IllegalArgumentException("VARCHAR field must have a size between 1 and 255 characters.");
        }
        return new MySQLType(MySQLTypes.VARCHAR.name(), size);
    }
    
    /**
     * Static initializer for <tt>MySQLTypeM</tt> TINYTEXT.
     * 
     * @return returns a new <tt>MySQLType</tt> of type TINYTEXT.
     */
    public static MySQLType TINYTEXT() { return new MySQLType(MySQLTypes.TINYTEXT.name(), 255); }
    
    /**
     * Static initializer for <tt>MySQLType</tt> TEXT.
     * 
     * @return returns a new <tt>MySQLType</tt> of type TEXT.
     */
    public static MySQLType TEXT() { return new MySQLType(MySQLTypes.TEXT.name(), 65535); }
    
    /**
     * Static initializer for <tt>MySQLType</tt> BLOB.
     * 
     * @return returns a new <tt>MySQLType</tt> of type BLOB.
     */
    public static MySQLType BLOB() { return new MySQLType(MySQLTypes.BLOB.name(), 65535); }
    
    /**
     * Static initializer for <tt>MySQLType</tt> BOOLEAN.
     * 
     * @return returns a new <tt>MySQLType</tt> of type BOOLEAN.
     */
    public static MySQLType BOOLEAN() { return new MySQLType(MySQLTypes.BOOLEAN.name(), 1); }

    /**
     * Static initializer for <tt>MySQLType</tt> TINYINT.
     * 
     * @param size The size of this TINYINT field. Valid range: 1-3.
     * @param signed Specify true for signed Integer, false for unsigned.
     * @return returns a new <tt>MySQLType</tt> of type TINYINT.
     */
    public static MySQLType TINYINT(Integer size, Boolean signed) {
        if(size == null || (size < 1 || size > 3)) {
            throw new IllegalArgumentException("TINYINT field must have a size between 1 and 3 digits.");
        } else if (signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.TINYINT.name(), size, signed);
    }
    
    /**
     * Static initializer for <tt>MySQLType</tt> SMALLINT.
     * 
     * @param size The size of this SMALLINT field. Valid range: 1-5.
     * @param signed Specify true for signed Integer, false for unsigned.
     * @return returns a new <tt>MySQLType</tt> of type SMALLINT.
     */
    public static MySQLType SMALLINT(Integer size, Boolean signed) {
        if(size == null || (size < 1 || size > 5)) {
            throw new IllegalArgumentException("SMALLINT field must have a size between 1 and 5 digits.");
        } else if (signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.SMALLINT.name(), size, signed);
    }
    
    /**
     * Static initializer for <tt>MySQLType</tt> MEDIUMINT.
     * 
     * @param size The size of this MEDIUMINT field. Valid range: 1-8.
     * @param signed Specify true for signed Integer, false for unsigned.
     * @return returns a new <tt>MySQLType</tt> of type MEDIUMINT.
     */
    public static MySQLType MEDIUMINT(Integer size, Boolean signed) {
        if(size == null || (size < 1 || size > 8)) {
            throw new IllegalArgumentException("MEDIUMINT field must have a size between 1 and 8 digits.");
        } else if (signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.MEDIUMINT.name(), size, signed);
    }
    
    /**
     * Static initializer for <tt>MySQLType</tt> INT.
     * 
     * @param size The size of this INT field. Valid range: 1-10.
     * @param signed Specify true for signed Integer, false for unsigned.
     * @return returns a new <tt>MySQLType</tt> of type INT.
     */
    public static MySQLType INT(Integer size, Boolean signed) {
        if(size == null || (size < 1 || size > 10)) {
            throw new IllegalArgumentException("INT field must have a size between 1 and 10 digits.");
        } else if (signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.INT.name(), size, signed);
    }
    
    /**
     * Static initializer for <tt>MySQLType</tt> FLOAT.
     * 
     * @param size The size of this FLOAT field. Valid range: 1-65.
     * @param decimal The amount of decimal places. Valid range: 1-30.
     * @param signed Specify true for signed Float, false for unsigned.
     * @return returns a new <tt>MySQLType</tt> of type TINYINT.
     */
    public static MySQLType FLOAT(Integer size, Integer decimal, Boolean signed) {
        if(size == null || (size < 1 || size > 65)) {
            throw new IllegalArgumentException("FLOAT field must have a size between 1 and 65 digits.");
        } else if (decimal == null || (decimal < 1 || decimal > 30)) {
            throw new IllegalArgumentException("FLOAT field must have a decimal size between 1 and 30 digits.");
        }else if (signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.FLOAT.name(), size, decimal, signed);
    }
    
    public static MySQLType DOUBLE(Integer size, Integer decimal, Boolean signed) {
        if(size == null || size < 1) {
            throw new IllegalArgumentException("DOUBLE field must have a size more than 1 digit.");
        } else if (decimal == null || decimal < 1) {
            throw new IllegalArgumentException("DOUBLE field must have a decimal size more than 1 digit.");
        } else if(signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.DOUBLE.name(), size, decimal, signed);
    }
    
    public static MySQLType DECIMAL(Integer size, Integer decimal, Boolean signed) {
        if(size == null || (size < 1 || size > 65)) {
            throw new IllegalArgumentException("DEICMAL field must have a size between 1 and 65 digits.");
        } else if (decimal == null || (decimal < 1 || decimal > 30)) {
            throw new IllegalArgumentException("DECIMAL field must have a decimal size between 1 and 30 digits.");
        }else if (signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.DECIMAL.name(), size, decimal, signed);
    }
    
    public static MySQLType DATE() {
        return new MySQLType(MySQLTypes.DATE.name(), 10);
    }
    
    public MySQLType DATETIME() {
        return new MySQLType(MySQLTypes.DATETIME.name(), 19);
    }
    
    public MySQLType TIMESTAMP() {
        return new MySQLType(MySQLTypes.TIMESTAMP.name(), 19);
    }
    
    public MySQLType TIME() {
        return new MySQLType(MySQLTypes.TIME.name(), 9);
    }
    
    @Override
    public String getType() { return type; }
    
    @Override
    public Boolean canAutoincrement() {
        if(type.equals(MySQLTypes.TINYINT.name()) || type.equals(MySQLTypes.SMALLINT.name()) || type.equals(MySQLTypes.MEDIUMINT.name()) || type.equals(MySQLTypes.INT.name())) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns the maximum size of information that this field will accept.
     * 
     * @return the maximum size of the field as <tt>Integer</tt> or <tt>0</tt> if there is none set.
     */
    public Integer getSize() { return size; }
    
    /**
     * Returns the maximum amount of decimal values that this field will truncate to.
     * 
     * @return the maximum number of decimal places in this field as <tt>Integer</tt> or <tt>0</tt> if there is none set.
     */
    public Integer getDecimal() { return decimal; }
    
    /**
     * Returns whether or not this data value is a SIGNED or UNSIGNED value.
     * 
     * @return <tt>true</tt> if field is SIGNED; <tt>false</tt> if UNSIGNED
     */
    public Boolean isSIGNED() { return signed; }
    
}