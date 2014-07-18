package us.drome.cobrasqlib;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColumnDef {
    public String name;
    public Type type;
    public int size;
    public int decimal;
    public boolean isPrimary = false;
    public boolean isAutoincrement = false;
    public boolean isNotNull = false;
    public boolean isUnsigned = false;
    
    public ColumnDef(String name, Type type, Options... options) {
        this(name, type, -1, -1, options);
    }
    
    public ColumnDef(String name, Type type, int size, Options... options) {
        this(name, type, size, -1, options);
    }
    
    public ColumnDef(String name, Type type, int size, int decimal, Options... options) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.decimal = decimal;
        
        for (Options opt : options) {
            if(opt.equals(Options.isAutoincrement)) {
                this.isAutoincrement = true;
            } else if(opt.equals(Options.isNotNull)) {
                this.isNotNull = true;
            } else if(opt.equals(Options.isPrimary)) {
                this.isPrimary = true;
            } else if(opt.equals(Options.isUnsigned)) {
                this.isUnsigned = true;
            }
        }
    }
    
    public static ColumnDef generateDef(ResultSetMetaData meta, DatabaseMetaData dbMeta) throws SQLException {
        ResultSet keyQuery = dbMeta.getPrimaryKeys(null, null, meta.getTableName(1));
        List<String> pKeys = new ArrayList<>();
        while(keyQuery.next()) {
            pKeys.add(keyQuery.getString("COLUMN_NAME"));
        }
        return new ColumnDef(meta.getColumnName(1),
                Type.parseType(meta.getColumnType(1)),
                meta.getPrecision(1),
                meta.getScale(1),
                (meta.isAutoIncrement(1) ? Options.isAutoincrement : null),
                (meta.isNullable(1) == 0 ? Options.isNotNull : null),
                (pKeys.contains(meta.getColumnName(1)) ? Options.isPrimary : null),
                (meta.isSigned(1) ? null : Options.isUnsigned));
    }
}
