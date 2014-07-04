package us.drome.cobrasqlib;

public class MySQLType implements DataType {
    enum MySQLTypes { CHAR, VARCHAR, TINYTEXT, TEXT, BLOB, BOOLEAN,
    TINYINT, SMALLINT, MEDIUMINT, INT, FLOAT, DOUBLE, DECIMAL,
    DATE, DATETIME, TIMESTAMP, TIME; }
    
    private final String type;
    private final Integer size;
    private final Integer decimal;
    private final Boolean signed;
    
    private MySQLType(String type) { this.type = type; this.size = 1; this.decimal = 0; this.signed = false; }
    
    private MySQLType(String type, Integer size) { this.type = type; this.size = size; this.decimal = 0; this.signed = false; }
    
    private MySQLType(String type, Integer size, Integer decimal) { this.type = type; this.size = size; this.decimal = decimal; this.signed = false; }
    
    private MySQLType(String type, Integer size, Boolean signed) { this.type = type; this.size = size; this.decimal = 0; this.signed = signed; }
    
    private MySQLType(String type, Integer size, Integer decimal, Boolean signed) { this.type = type; this.size = size; this.decimal = decimal; this.signed = signed; }
    
    public static MySQLType CHAR(Integer size) {
        if(size == null || (size < 1 || size > 255)) {
            throw new IllegalArgumentException("CHAR field must have a size between 1 and 255 characters.");
        }
        return new MySQLType(MySQLTypes.CHAR.name(), size);
    }
    
    public static MySQLType VARCHAR(Integer size) {
        if(size == null || (size < 1 || size > 255)) {
            throw new IllegalArgumentException("VARCHAR field must have a size between 1 and 255 characters.");
        }
        return new MySQLType(MySQLTypes.VARCHAR.name(), size);
    }
    
    public static MySQLType TINYTEXT() { return new MySQLType(MySQLTypes.TINYTEXT.name(), 255); }
    
    public static MySQLType TEXT() { return new MySQLType(MySQLTypes.TEXT.name(), 65535); }
    
    public static MySQLType BLOB() { return new MySQLType(MySQLTypes.BLOB.name(), 65535); }
    
    public static MySQLType BOOLEAN() { return new MySQLType(MySQLTypes.BOOLEAN.name()); }
    
    public static MySQLType TINYINT(Integer size, Boolean signed) {
        if(size == null || (size < 1 || size > 3)) {
            throw new IllegalArgumentException("TINYINT field must have a size between 1 and 3 digits.");
        } else if (signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.TINYINT.name(), size, signed);
    }
    
    public static MySQLType SMALLINT(Integer size, Boolean signed) {
        if(size == null || (size < 1 || size > 5)) {
            throw new IllegalArgumentException("SMALLINT field must have a size between 1 and 5 digits.");
        } else if (signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.SMALLINT.name(), size, signed);
    }
    
    public static MySQLType MEDIUMINT(Integer size, Boolean signed) {
        if(size == null || (size < 1 || size > 8)) {
            throw new IllegalArgumentException("MEDIUMINT field must have a size between 1 and 8 digits.");
        } else if (signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.MEDIUMINT.name(), size, signed);
    }
    
    public static MySQLType INT(Integer size, Boolean signed) {
        if(size == null || (size < 1 || size > 10)) {
            throw new IllegalArgumentException("INT field must have a size between 1 and 10 digits.");
        } else if (signed == null) {
            throw new NullPointerException("Field 'signed' cannot be null.");
        }
        return new MySQLType(MySQLTypes.INT.name(), size, signed);
    }
    
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