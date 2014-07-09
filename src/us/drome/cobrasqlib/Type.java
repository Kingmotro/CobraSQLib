package us.drome.cobrasqlib;

public enum Type {
    BIT(1,64,0,0,1,0),
    TINYINT(1,3,0,0,1,0),
    SMALLINT(0,0,0,0,0,0),
    INTEGER(0,0,0,0,0,0),
    BIGINT(0,0,0,0,0,0),
    FLOAT(0,0,0,0,0,0),
    REAL(0,0,0,0,0,0),
    DOUBLE(0,0,0,0,0,0),
    NUMERIC(0,0,0,0,0,0),
    DECIMAL(0,0,0,0,0,0),
    CHAR(0,0,0,0,0,0),
    VARCHAR(0,0,0,0,0,0),
    LONGVARCHAR(0,0,0,0,0,0),
    DATE(0,0,0,0,0,0),
    TIME(0,0,0,0,0,0),
    TIMESTAMP(0,0,0,0,0,0),
    BINARY(0,0,0,0,0,0),
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
    
    private Type(int minPrecision, int maxPrecision, int minScale, int maxScale, int defPrecision, int defScale) {
        this.minPrecision = minPrecision;
        this.maxPrecision = maxPrecision;
        this.minScale = minScale;
        this.maxScale = maxScale;
        this.defPrecision = defPrecision;
        this.defScale = defScale;
    }
    
    public int getMinPrecision() { return minPrecision; }
    
    public int getMaxPrecision() { return maxPrecision; }
    
    public int getMinScale() { return minScale; }
    
    public int getMaxScale() { return maxScale; }
    
    public int getDefaultPrecision() { return defPrecision; }
    
    public int getDefaultScale() { return defScale; }
}
