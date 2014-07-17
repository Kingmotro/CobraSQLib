package us.drome.cobrasqlib;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.Struct;
import java.sql.Timestamp;

public enum Type {
    BIT(64, 0, true, false, false, Boolean.class, byte[].class),
    TINYINT(255, 0, true, false, true, Integer.class, Boolean.class),
    SMALLINT(255, 0, true, false, true, Integer.class),
    INTEGER(255, 0, true, false, true, Integer.class, Long.class),
    BIGINT(255, 0, true, false, true, Long.class, BigInteger.class),
    FLOAT(65, 30, true, true, true, Float.class),
    REAL(65,30, true, true, true, Double.class, Float.class),
    DOUBLE(65, 30, true, false, true, Double.class),
    NUMERIC(65, 30, true, true, true, BigDecimal.class),
    DECIMAL(65, 30, true, true, true, BigDecimal.class),
    CHAR(255, 0, true, false, false, String.class),
    VARCHAR(65535, 0, true, false, false, String.class),
    LONGVARCHAR(0, 0, false, false, false, String.class),
    DATE(6, 0, true, false, false, Date.class),
    TIME(6, 0, true, false, false, Timestamp.class),
    TIMESTAMP(6, 0, true, false, false, Timestamp.class),
    BINARY(255, 0, true, false, false, byte[].class),
    VARBINARY(255, 0, true, false, false, byte[].class),
    LONGBINARY(255, 0, true, false, false, byte[].class),
    NULL(0, 0, false, false, false, Object.class),
    OTHER(0, 0, false, false, false, Object.class),
    JAVA_OBJECT(0, 0, false, false, false, Object.class),
    DISTINCT(0, 0, false, false, false, Object.class),
    STRUCT(0, 0, false, false, false, Struct.class),
    ARRAY(0, 0, false, false, false, Array.class),
    BLOB(0, 0, false, false, false, Blob.class),
    CLOB(0, 0, false, false, false, Clob.class),
    REF(0, 0, false, false, false, Ref.class),
    DATALINK(0, 0, false, false, false, java.net.URL.class),
    BOOLEAN(0, 0, false, false, false, Boolean.class),
    ROWID(0, 0, false, false, false, Integer.class),
    NCHAR(255, 0, true, false, false, String.class),
    NVARCHAR(65535, 0, true, false, false, String.class),
    LONGNVARCHAR(0, 0, false, false, false, String.class),
    NCLOB(0, 0, false, false, false, NClob.class),
    SQLXML(0, 0, false, false, false, java.sql.SQLXML.class);
    
    private final int maxSize;
    private final int maxDecimal;
    private final boolean canHaveSize;
    private final boolean canHaveDecimal;
    private final boolean isSignable;
    private final Class[] returnTypes;
    
    private Type(int maxSize, int maxDecimal, boolean canHaveSize, boolean canHaveDecimal, boolean isSignable, Class... returnTypes) {
        this.maxSize = maxSize;
        this.maxDecimal = maxDecimal;
        this.canHaveSize = canHaveSize;
        this.canHaveDecimal = canHaveDecimal;
        this.isSignable = isSignable;
        this.returnTypes = returnTypes;
    }
    
    public String getName() { return toString(); }
    
    public int getMaxSize() { return this.maxSize; }
    
    public int getMaxDecinal() { return this.maxDecimal; }
    
    public Class[] getReturnTypes() { return returnTypes; }
    
    public void verifyIntegrity(int size, int decimal, boolean isPrimary, boolean isAutoincrement, boolean isNullable, boolean isUnsigned) throws InvalidSQLConfigException {
        if(canHaveSize && (size < 0 || size > this.maxSize)) {
            throw new InvalidSQLConfigException("Size parameter for type " + this.getName() + " must be between 0 and " + this.maxSize + ".");
        } else if (!canHaveSize && size != -1) {
            throw new InvalidSQLConfigException("Type " + this.getName() + " cannot have a custom size paramter.");
        } else if(canHaveDecimal && (decimal < 0 || decimal > this.maxDecimal))  {
            throw new InvalidSQLConfigException("Decimal paramter for type " + this.getName() + " must be between 0 and " + this.maxDecimal + ".");
        } else if(!canHaveDecimal && size != -1) {
            throw new InvalidSQLConfigException("Type " + this.getName() + " cannot have a custom decimal paramter.");
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
