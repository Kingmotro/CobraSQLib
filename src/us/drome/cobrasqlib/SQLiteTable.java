package us.drome.cobrasqlib;

/**
 * SQLiteTable class provides the ability to create a new Table for the SQLite Engine. Any additional SQLite-only functions would be added here.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class SQLiteTable extends Table {
    protected SQLiteTable(SQLiteEngine parent, String name, ColumnDef... columns) {
        super(parent, name, columns);
    }  
}
