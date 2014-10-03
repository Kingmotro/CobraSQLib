package us.drome.cobrasqlib;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The <tt>ColumnDef</tt> class contains all the properties of a column for a database table.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class ColumnDef {
    public String name;
    public Type type;
    public int size;
    public int decimal;
    public boolean isPrimary = false;
    public boolean isAutoincrement = false;
    public boolean isNotNull = false;
    public boolean isUnsigned = false;
    
    /**
     * Initialize a new ColumnDef that establishes the parameters of a column.
     * @param name The column's name.
     * @param type The <tt>Type</tt> of data it can contain.
     * @param options An array of additional options from the <tt>Option</tt> enum.
     */
    public ColumnDef(String name, Type type, Options... options) {
        this(name, type, -1, -1, options);
    }
    
    /**
     * Initialize a new ColumnDef that establishes the parameters of a column.
     * @param name The column's name.
     * @param type The <tt>Type</tt> of data it can contain.
     * @param size The size of characters/digits this column can hold.
     * @param options An array of additional options from the <tt>Option</tt> enum.
     */
    public ColumnDef(String name, Type type, int size, Options... options) {
        this(name, type, size, -1, options);
    }
    
    /**
     * Initialize a new ColumnDef that establishes the parameters of a column.
     * @param name The column's name.
     * @param type The <tt>Type</tt> of data it can contain.
     * @param size The size of characters/digits this column can hold.
     * @param decimal The amount of decimal places allowed.
     * @param options An array of additional options from the <tt>Option</tt> enum.
     */
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
    
    /**
     * A protected method to generate all column definitions from a provided table name and database meta data.
     * @param table The name of the table to generate column definitions from.
     * @param dbMeta The metadata object retrieved from the database.
     * @return An array of column definitions that represent the columns and their properties.
     * @throws SQLException
     */
    protected static ColumnDef[] generateDefs(String table, DatabaseMetaData dbMeta) throws SQLException {
        ResultSet keyQuery = dbMeta.getPrimaryKeys(null, null, table);
        List<String> pKeys = new ArrayList<>();
        List<ColumnDef> definitions = new ArrayList<>();
        while(keyQuery.next()) {
            pKeys.add(keyQuery.getString("COLUMN_NAME"));
        }
        
        ResultSet columnQuery = dbMeta.getColumns(null, null, table, "%");
        while(columnQuery.next()) {
            definitions.add(new ColumnDef(
                    columnQuery.getString("COLUMN_NAME"),
                    Type.parseType(columnQuery.getInt("DATA_TYPE")),
                    columnQuery.getInt("COLUMN_SIZE"),
                    columnQuery.getInt("DECIMAL_DIGITS"),
                    columnQuery.getString("IS_AUTOINCREMENT").equals("YES") ? Options.isAutoincrement : null,
                    columnQuery.getString("IS_NULLABLE").equalsIgnoreCase("NO") ? Options.isNotNull : null,
                    (pKeys.contains(columnQuery.getString("COLUMN_NAME")) ? Options.isPrimary : null),
                    columnQuery.getString("TYPE_NAME").toLowerCase().contains("unsigned") ? Options.isUnsigned : null
            ));
        }
        
        return definitions.toArray(new ColumnDef[definitions.size()]);
    }
    
    /**
     * Function returns the Java class that data from a column with this definition can be cast to.
     * If there are multiple possible return types, the conditions are checked and
     * the only valid return type for the columns properties is returned.
     * @return The Java <tt>Class</tt> that represents the return type.
     */
    public Class getReturnType() {
        if(this.type.equals(Type.BIT) && this.size > 1) {
            return type.getReturnTypes()[1];
        } else if(this.type.equals(Type.TINYINT) && this.size == 1) {
            return type.getReturnTypes()[1];
        } else if(this.type.equals(Type.INTEGER) && this.isUnsigned) {
            return type.getReturnTypes()[1];
        } else if(this.type.equals(Type.BIGINT) && this.isUnsigned) {
            return type.getReturnTypes()[1];
        } else {
            return type.getReturnTypes()[0];
        }
    }
}
