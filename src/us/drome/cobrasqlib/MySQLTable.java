package us.drome.cobrasqlib;

/**
 * Represents a table for a MySQL database including MySQL-only functionality.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class MySQLTable extends Table {
    
    protected MySQLTable(MySQLEngine parent, String name, ColumnDef... columns) {
        super(parent, name, columns);
    }

    /**
     * Adds a new column to this <tt>Table</tt> with the provided definition.
     * @param definition The definition of the new column.
     */
    public void addColumn(ColumnDef definition) {
        String type = definition.type.getName();
        if(definition.size >= 0 || definition.decimal >= 0) {
            type += "(";
            type += definition.size >= 0 ? definition.size : "";
            type += definition.decimal >= 0 ? "," + definition.decimal : "";
            type += ")";
        }
        String constraints = definition.isUnsigned ? " UNSIGNED" : "";
        constraints += definition.isNotNull ? " NOT NULL" : "";
        constraints += definition.isPrimary ? " PRIMARY KEY" : "";
        constraints += definition.isAutoincrement ? " AUTO_INCREMENT" : "";
        parent.runAsyncUpdate("ALTER TABLE " + name + " ADD " + definition.name + " " + type + " " + constraints);
    }

    /**
     * Modifies the specified column in this <tt>Table</tt> to the new definition.
     * @param name The name of the column to modify.
     * @param definition The new definition of the column.
     */
    public void modifyColumn(String name, ColumnDef definition) {
        String type = definition.type.getName();
        if(definition.size >= 0 || definition.decimal >= 0) {
            type += "(";
            type += definition.size >= 0 ? definition.size : "";
            type += definition.decimal >= 0 ? "," + definition.decimal : "";
            type += ")";
        }
        String constraints = definition.isUnsigned ? " UNSIGNED" : "";
        constraints += definition.isNotNull ? " NOT NULL" : "";
        constraints += definition.isPrimary ? " PRIMARY KEY" : "";
        constraints += definition.isAutoincrement ? " AUTOINCREMENT" : "";
        parent.runAsyncUpdate("ALTER TABLE " + name + " MODIFY COLUMN " + definition.name + " " + type + " " + constraints);
    }

    /**
     * Removes the specified column from the <tt>Table</tt>.
     * @param name The name of the column to remove.
     */
    public void removeColumn(String name) {
         parent.runAsyncUpdate("ALTER TABLE " + this.name + " DROP COLUMN " + name);
    }
    
}
