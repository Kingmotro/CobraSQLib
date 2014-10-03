package us.drome.cobrasqlib;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

/**
 * Abstract Table class provides the basic necessary functions and properties behind all database tables.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public abstract class Table {
    protected final SQLEngine parent;
    protected String name;
    protected final ArrayList<ColumnDef> columns;
    
    /**
     * Construct a new <tt>Table</tt> object.
     * @param parent The database where this <tt>Table</tt> is located.
     * @param name The name of this <tt>Table</tt>.
     * @param columns A collection of <tt>ColumnDef<tt> objects that define the table schema.
     */
    protected Table(SQLEngine parent, String name, ColumnDef... columns) {
        this.parent = parent;
        this.name = name;
        this.columns = (ArrayList<ColumnDef>) Arrays.asList(columns);
    }
    
    /**
     * @return The name of this <tt>Table</tt> instance.
     */
    public String getName() { return name; }
    
    /**
     * Renames this <tt>Table</tt> instance to the specified name.
     * @param newName The <tt>Table<tt>'s new name.
     */
    protected void rename(String newName) { name = newName; }
    
    /**
     * @return The <tt>Set</tt> of <tt>ColumnDef</tt> objects that define the <tt>Table</tt>'s columns. 
     */
    public List<ColumnDef> getColumns() { return columns; }
    
    /**
     * Get the definition for the specified column.
     * @param name The name of the column to return
     * @return A <tt>ColumnDef</tt> object containing the properties of the column.
     */
    public ColumnDef getColumn(String name) { 
        for(ColumnDef def : columns) {
            if(def.name.equalsIgnoreCase(name)) {
                return def;
            }
        }
        return null;
    }
    
    /**
     * Retrieves the <tt>Table</tt>'s primary key column definition.
     * 
     * @return The <tt>ColumnDef</tt> object for the <tt>Table</tt>'s primary key column.
     */
    public ColumnDef getPrimaryKey() {
        for(ColumnDef def : columns) {
            if(def.isPrimary)
                return def;
        }
        return null;
    }
    
    /**
     * Runs a query against this <tt>Table</tt> that returns all <tt>Row</tt>s that match the specified column and value.
     * 
     * @param selectColumns The names of the columns to be included in the result.
     * @param whereColumn The name of the column to search.
     * @param whereValue The Object to search for.
     * @param callback Method to run on query completion. Must accept a <tt>List&lt;Row&gt;</tt> as a parameter.
     */
    public void getRows(String[] selectColumns, final String whereColumn, final Object whereValue, final Callback callback) {
        final String name = this.name;
        String select = "";
        for(int i=0; i<selectColumns.length; i++) {
            select += selectColumns[i];
            if(i < selectColumns.length-1)
                select += ",";
        }
        final String selectString = select;
        parent.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<Row> result = parent.runQuery("SELECT " + selectString + " FROM " + name + " WHERE " + whereColumn + " = '" + whereValue.toString() + "'");
                try {
                    callback.invoke(result);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    parent.logger.log(Level.SEVERE, ex.getMessage());
                }
            }
        });
    }
    
    /**
     * Runs a query against this <tt>Table</tt> that returns all <tt>Row</tt>s that match the specified column and value.
     * 
     * @param column The name of the column to search.
     * @param value The Object to search for.
     * @param callback Method to run on query completion. Must accept a <tt>List&lt;Row&gt;</tt> as a parameter.
     */
    public void getRows(final String column, final Object value, final Callback callback) {
        getRows(new String[] {"*"}, column, value, callback);
    }
    
    /**
     * Runs a query against this <tt>Table</tt> that returns all <tt>Row</tt>s that match the specified column and value. This method assumes
     * that you are searching in the Primary Key column.
     * 
     * @param match The name of the column to search.
     * @param callback Method to run on query completion. Must accept a <tt>List&lt;Row&gt;</tt> as a parameter.
     */
    public void getRows(final Object match, final Callback callback) {
        getRows(this.getPrimaryKey().name, match, callback);
    }
    
    /**
     * Runs a query against this <tt>Table</tt> that matches a specified column and value and returns all values from a selected column.
     * 
     * @param selectColumn The column to return results from.
     * @param whereColumn The name of the column to search.
     * @param whereValue The Object to search for.
     * @param callback Method to run on query completion. Must accept a <tt>List&lt;Column&gt;</tt> as a parameter.
     */
    public void getValues(final String selectColumn, final String whereColumn, final Object whereValue, final Callback callback) {
        final String name = this.name;
        parent.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<Row> result = parent.runQuery("SELECT " + selectColumn + " FROM " + name + " WHERE " + whereColumn + " = '" + whereValue.toString() + "'");
                List<Column> finalResult = new ArrayList<>();
                for(Row row : result) {
                    finalResult.add(row.getColumn(name));
                }
                try {
                    callback.invoke(finalResult);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    parent.logger.log(Level.SEVERE, ex.getMessage());
                }
            }
        });
    }
    
    /**
     * Insert the specified values as a new row into the <tt>Table</tt>.
     * @param values The column values, in order, to insert into the <tt>Table</tt>.
     */
    public void insert(String... values) {
        String columnString = "(";
        String valueString = "(";
        int count = 0;
        Iterator<ColumnDef> defs = columns.iterator();
        while(defs.hasNext()) {
            ColumnDef def = defs.next();
            if(def.isPrimary && def.isAutoincrement) {
                continue;
            } else {
                columnString += def.name;
                valueString += "'" + values[count] + "'";
                count++;
            }
            
            if(defs.hasNext()) {
                columnString += ",";
                valueString += ",";
            } else {
                columnString += ")";
                valueString += ")";
            }
        }
        parent.runAsyncUpdate("INSERT INTO " + name + " " + columnString + " VALUES " + valueString);
    }
    
    /**
     * Update the specified <tt>Row</tt> in the <tt>Table</tt>.
     * 
     * @param row The <tt>Row</tt> to update.
     */
    public void updateRow(Row row) {
        String pKey = this.getPrimaryKey().name;
        String setString = "SET";
        Iterator<Column> colIt = row.Iterator();
        while(colIt.hasNext()) {
            Column next = colIt.next();
            if(!colIt.next().isPrimaryKey()) {
                setString += " " + next.getName() + "='" + next.getData().toString() + "'";
                if(colIt.hasNext()) {
                    setString +=",";
                }
            }
        }
        parent.runAsyncUpdate("UPDATE " + name + setString + " WHERE " + pKey + " ='" + row.getColumn(pKey).getData().toString() + "'");
    }
    
    /**
     * Update a specific column in the <tt>Table</tt> to a new value where the column's value matches the provided value.
     * 
     * @param column The name of the column to update.
     * @param oldValue The Object to search for.
     * @param newValue The Object to replace the old value with.
     */
    public void update(String column, Object oldValue, Object newValue) {
        parent.runAsyncUpdate("UPDATE " + name + " SET " + column + "='" + newValue.toString() + "' WHERE " + column + "='" + oldValue.toString());
    }
    
    /**
     * Remove a specific Row from the <tt>Table</tt>.
     *  
     * @param row The <tt>Row</tt> to delete.
     */
    public void deleteRow(Row row) {
        String whereString = "WHERE";
        Iterator<Column> colIt = row.Iterator();
        while(colIt.hasNext()) {
            Column next = colIt.next();
            whereString += " " + next.getName() + " ='" + next.getData().toString() + "'";
            if(colIt.hasNext()) {
                whereString += ",";
            }
        }
        parent.runAsyncUpdate("DELETE FROM " + name + whereString);
    }
    
    /**
     * Removes all <tt>Row</tt>s from the <tt>Table</tt> where the specified column matches the provided value.
     * 
     * @param column The name of the column to search.
     * @param value The Object to search for.
     */
    public void delete(String column, Object value) {
        parent.runAsyncUpdate("DELETE FROM " + name + " WHERE " + column + "='" + value.toString() + "'");
    }
}