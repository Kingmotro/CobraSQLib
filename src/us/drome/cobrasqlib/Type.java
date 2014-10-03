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
import java.sql.Types;

/**
 * The <tt>Type</tt> class is a wrapper for the java.sql.Types class. This wrapper is designed to hold default and maximum values
 * for data type properties. As well as containing the possible Java classes that the data types can return. It also provides
 * functions to fetch one of these types by providing the java.sql.Types int designation, as well as verify that a provided
 * set of column properties is valid for the data type.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public enum Type {
    BIT(64, 0, true, false, false, Types.BIT, Boolean.class, byte[].class),
    TINYINT(255, 0, true, false, true, Types.TINYINT,Integer.class, Boolean.class),
    SMALLINT(255, 0, true, false, true, Types.SMALLINT,Integer.class),
    INTEGER(255, 0, true, false, true, Types.INTEGER, Integer.class, Long.class),
    BIGINT(255, 0, true, false, true, Types.BIGINT, Long.class, BigInteger.class),
    FLOAT(65, 30, true, true, true, Types.FLOAT, Float.class),
    REAL(65,30, true, true, true, Types.REAL, Double.class, Float.class),
    DOUBLE(65, 30, true, false, true, Types.DOUBLE, Double.class),
    NUMERIC(65, 30, true, true, true, Types.NUMERIC, BigDecimal.class),
    DECIMAL(65, 30, true, true, true, Types.DECIMAL, BigDecimal.class),
    CHAR(255, 0, true, false, false, Types.CHAR, String.class),
    VARCHAR(65535, 0, true, false, false, Types.VARCHAR, String.class),
    LONGVARCHAR(0, 0, false, false, false, Types.LONGNVARCHAR, String.class),
    DATE(6, 0, true, false, false, Types.DATE, Date.class),
    TIME(6, 0, true, false, false, Types.TIME, Timestamp.class),
    TIMESTAMP(6, 0, true, false, false, Types.TIMESTAMP, Timestamp.class),
    BINARY(255, 0, true, false, false, Types.BINARY, byte[].class),
    VARBINARY(255, 0, true, false, false, Types.VARBINARY, byte[].class),
    LONGVARBINARY(255, 0, true, false, false, Types.LONGVARBINARY, byte[].class),
    NULL(0, 0, false, false, false, Types.NULL, Object.class),
    OTHER(0, 0, false, false, false, Types.OTHER, Object.class),
    JAVA_OBJECT(0, 0, false, false, false, Types.JAVA_OBJECT, Object.class),
    DISTINCT(0, 0, false, false, false, Types.DISTINCT, Object.class),
    STRUCT(0, 0, false, false, false, Types.STRUCT, Struct.class),
    ARRAY(0, 0, false, false, false, Types.ARRAY, Array.class),
    BLOB(0, 0, false, false, false, Types.BLOB, Blob.class),
    CLOB(0, 0, false, false, false, Types.CLOB, Clob.class),
    REF(0, 0, false, false, false, Types.REF, Ref.class),
    DATALINK(0, 0, false, false, false, Types.DATALINK, java.net.URL.class),
    BOOLEAN(0, 0, false, false, false, Types.BOOLEAN, Boolean.class),
    ROWID(0, 0, false, false, false, Types.ROWID, Integer.class),
    NCHAR(255, 0, true, false, false, Types.NCHAR, String.class),
    NVARCHAR(65535, 0, true, false, false, Types.NVARCHAR, String.class),
    LONGNVARCHAR(0, 0, false, false, false, Types.LONGNVARCHAR, String.class),
    NCLOB(0, 0, false, false, false, Types.NCLOB, NClob.class),
    SQLXML(0, 0, false, false, false, Types.SQLXML, java.sql.SQLXML.class);
    
    private final int maxSize;
    private final int maxDecimal;
    private final boolean canHaveSize;
    private final boolean canHaveDecimal;
    private final boolean isSignable;
    private final Class[] returnTypes;
    private final int typeID;
    
    private Type(int maxSize, int maxDecimal, boolean canHaveSize, boolean canHaveDecimal, boolean isSignable, int typeID, Class... returnTypes) {
        this.maxSize = maxSize;
        this.maxDecimal = maxDecimal;
        this.canHaveSize = canHaveSize;
        this.canHaveDecimal = canHaveDecimal;
        this.isSignable = isSignable;
        this.returnTypes = returnTypes;
        this.typeID = typeID;
    }
    
    /**
     * Returns the name of this data type as a <tt>String</tt>.
     * @return
     */
    public String getName() { return toString(); }
    
    /**
     * Provides the maximum size this data type allows.
     * @return
     */
    public int getMaxSize() { return this.maxSize; }
    
    /**
     * Provides the maximum amount of decimal places this data type allows.
     * @return
     */
    public int getMaxDecinal() { return this.maxDecimal; }
    
    /**
     * Fetches an array of Java classes that this data type can return.
     * @return
     */
    public Class[] getReturnTypes() { return returnTypes; }
    
    /**
     * Function to test the validity of the combination of settings being used to create a column definition of this type.
     * 
     * @param size The size of the column
     * @param decimal The amount of decimal values
     * @param isPrimary Is this a primary key
     * @param isAutoincrement Will the column increment automatically
     * @param isNullable Can the column accept null values
     * @param isUnsigned Does this column accept only positive values
     * @throws InvalidSQLConfigException Throws this exception if the column type cannot possess the attribute specified.
     */
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
    
    /**
     * Returns a <tt>Type</tt> object representing the equivalent value from java.sql.Types that the integer value matches.
     * @param typeID The value from java.sql.Types
     * @return The matching <tt>Type</tt> object
     */
    public static Type parseType(int typeID) {
        for(Type type : Type.values()) {
            if(type.typeID == typeID) {
                return type;
            }
        }
        return null;
    }
}
