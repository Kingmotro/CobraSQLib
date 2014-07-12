package us.drome.cobrasqlib;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

public enum Type {
    BIT(1,64,0,0,1,0, false, Boolean.class, byte[].class),
    TINYINT(1,3,0,0,1,0, true, Integer.class, Boolean.class),
    SMALLINT(0,0,0,0,0,0, true, Integer.class),
    INTEGER(0,0,0,0,0,0, true, Integer.class, Long.class),
    BIGINT(0,0,0,0,0,0, true, Long.class, BigInteger.class),
    FLOAT(0,0,0,0,0,0, true, Float.class),
    REAL(0,0,0,0,0,0, true, Double.class, Float.class),
    DOUBLE(0,0,0,0,0,0, true, Double.class),
    NUMERIC(0,0,0,0,0,0, true, BigDecimal.class),
    DECIMAL(0,0,0,0,0,0, true, BigDecimal.class),
    CHAR(0,0,0,0,0,0, String.class),
    VARCHAR(0,0,0,0,0,0, String.class),
    LONGVARCHAR(0,0,0,0,0,0, String.class),
    DATE(0,0,0,0,0,0, Date.class),
    TIME(0,0,0,0,0,0, Timestamp.class),
    TIMESTAMP(0,0,0,0,0,0, Timestamp.class),
    BINARY(0,0,0,0,0,0, byte[].class),
    VARBINARY(0,0,0,0,0,0),
    LONGBINARY(0,0,0,0,0,0),
    NULL(0,0,0,0,0,0),
    OTHER(0,0,0,0,0,0),
    JAVA_OBJECT(0,0,0,0,0,0),
    DISTINCT(0,0,0,0,0,0),
    STRUCT(0,0,0,0,0,0),
    ARRAY(0,0,0,0,0,0),
    BLOB(0,0,0,0,0,0),
    CLOB(0,0,0,0,0,0),
    REF(0,0,0,0,0,0),
    DATALINK(0,0,0,0,0,0),
    BOOLEAN(1,1,0,0,1,0),
    ROWID(0,0,0,0,0,0),
    NCHAR(0,0,0,0,0,0),
    NVARCHAR(0,0,0,0,0,0),
    LONGNVARCHAR(0,0,0,0,0,0),
    NCLOB(0,0,0,0,0,0),
    SQLXML(0,0,0,0,0,0);
    
    private final int minPrecision;
    private final int maxPrecision;
    private final int minScale;
    private final int maxScale;
    private final int defPrecision;
    private final int defScale;
    private final boolean isSignable;
    private final Class[] returnTypes;
    
    private Type(int minPrecision, int maxPrecision, int minScale, int maxScale, int defPrecision, int defScale, boolean isSignable, Class... returnTypes) {
        this.minPrecision = minPrecision;
        this.maxPrecision = maxPrecision;
        this.minScale = minScale;
        this.maxScale = maxScale;
        this.defPrecision = defPrecision;
        this.defScale = defScale;
        this.isSignable = isSignable;
        this.returnTypes = returnTypes;
    }
    
    private Type(int minPrecision, int maxPrecision, int minScale, int maxScale, int defPrecision, int defScale, Class... returnTypes) {
        this(minPrecision, maxPrecision, minScale, maxScale, defPrecision, defScale, false, returnTypes);
    }
    
    public String getName() { return toString(); }
    
    public int getMinPrecision() { return minPrecision; }
    
    public int getMaxPrecision() { return maxPrecision; }
    
    public int getMinScale() { return minScale; }
    
    public int getMaxScale() { return maxScale; }
    
    public int getDefaultPrecision() { return defPrecision; }
    
    public int getDefaultScale() { return defScale; }
    
    public void verifyIntegrity(int precision, int scale, boolean isPrimary, boolean isAutoincrement, boolean isNullable, boolean isUnsigned) throws InvalidSQLConfigException {
        if(precision > this.maxPrecision || precision < this.minPrecision) {
            throw new InvalidSQLConfigException("Precision is not within accepted values for type " + this.getName() + " of " + this.getMinPrecision() + "-" + this.getMaxPrecision() + ".");
        } else if(scale > this.maxScale || scale < this.minScale) {
            throw new InvalidSQLConfigException("Scale is not within accepted values for type " + this.getName() + " of " + this.getMinScale() + "-" + this.getMaxScale() + ".");
        } else if(isUnsigned && !this.isSignable) {
            throw new InvalidSQLConfigException("Field cannot be UNSIGNED with type " + this.getName());
        } else if(isPrimary && isNullable) {
            throw new InvalidSQLConfigException("Primary key field cannot be Nullable.");
        } else if(isAutoincrement && !isPrimary) {
            throw new InvalidSQLConfigException("Auto Increment field must be the Primary Key.");
        } else if(isAutoincrement && !this.getName().equals("INTEGER")) {
            throw new InvalidSQLConfigException("Auto Increment field must be of type INTEGER.");
        } else if(isAutoincrement && !isUnsigned) {
            throw new InvalidSQLConfigException("Auto increment field cannot be a SIGNED INTEGER.");
        }
    }
}
