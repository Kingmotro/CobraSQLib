package us.drome.cobrasqlib;

/**
 * An enum interface to provide for the specification of the various column data types present across the different SQL implementations.
 * 
 * @author TheAcademician
 */
public interface DataType {
    public String getType();
}

enum SQLiteType implements DataType { TEXT, NUMERIC, INTEGER, REAL, NONE;
    @Override
    public String getType() { return toString(); }
};
    
enum MySQLType implements DataType { CHAR, VARCHAR, TINYTEXT, TEXT, BLOB, MEDIUMTEXT, MEDIUMBLOB, LONGTEXT, LONGBLOB, ENUM, SET,
    TINYINT, SMALLINT, MEDIUMINT, INT, BIGINT, FLOAT, DOUBLE, DECIMAL,
    DATE, DATETIME, TIMESTAMP, TIME;
    @Override
    public String getType() { return toString(); } 
};
    
enum PostgreSQL implements DataType { CHAR;
    @Override
    public String getType() { return toString(); }
};
