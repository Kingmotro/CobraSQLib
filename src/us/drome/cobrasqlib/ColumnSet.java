package us.drome.cobrasqlib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class represents a collection of Column objects that define the schema of a database Table.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class ColumnSet implements Set<Column>{
    private HashSet<Column> columns;
    private boolean hasPrimary;
    
    /**
     * Constructor that accepts any <tt>Collection</tt> of type <tt>Column</tt>.
     * This is used to quickly initialize a <tt>ColumnSet</tt> from an already established <tt>Table</tt>.
     * 
     * @param columns a generic <tt>Collection</tt> of <tt>Column</tt> objects
     */
    public ColumnSet(Collection<Column> columns) {
        this.columns = new HashSet<>();
        hasPrimary = false;
        this.columns.addAll(columns);
    }
    
    /**
     * Constructor that accepts no parameters. This is used to create a <tt>ColumnSet</tt> that can be used to create a <tt>Table</tt>.
     */
    public ColumnSet() {
        columns = new HashSet<>();
        hasPrimary = false;
    }
    
    /**
     * Returns a <tt>boolean</tt> that indicates whether this <tt>ColumnSet</tt> already has a set primary key.
     * <tt>ColumnSet</tt>s are only capable of having one primary key.
     * 
     * @return a <tt>boolean</tt> value indicating if this <tt>ColumnSet</tt> has a primary key
     */
    public boolean hasPrimary() { return hasPrimary; }
    
    /**
     * Returns a generic Collection of Column objects that can be cast as needed.
     * 
     * @return a <tt>Collection</tt> of <tt>Column</tt> objects
     */
    public Collection<Column> getColumns() { return columns; }
    
    /**
     * Returns a <tt>Column</tt> object that has the name specified.
     * 
     * @param name the name of the <tt>Column</tt> you wish to retrieve
     * @return the specified <tt>Column</tt>
     */
    public Column getColumn(String name) {
        for(Column c : columns) {
           if(c.getName().equalsIgnoreCase(name)) {
               return c;
           } 
        }
        return null;
    }
    
    /**
     * Returns a <tt>Collection</tt> of type <tt>String</tt> that holds the names of each <tt>Column</tt> in this <tt>ColumnSet</tt>.
     * 
     * @return a <tt>Collection</tt> of <tt>String</tt> containing the names of each <tt>Column</tt>
     */
    public Collection<String> getColumnNames() {
        Collection<String> names = new HashSet<>();
        for(Column c : columns) {
            names.add(c.getName());
        }
        return names;
    }

    @Override
    public int size() { return columns.size(); }

    @Override
    public boolean isEmpty() { return columns.isEmpty(); }

    @Override
    public boolean contains(Object o) { return columns.contains(o); }

    @Override
    public Iterator iterator() { return columns.iterator(); }

    @Override
    public Object[] toArray() { return columns.toArray(); }

    @Override
    public Object[] toArray(Object[] a) { return columns.toArray(a); }

    @Override
    public boolean add(Column c) {
        if(columns.contains(c)) {
            return false;
        } else {
            if(c.isPrimary()) {
                if(hasPrimary) {
                    throw new IllegalArgumentException("Another column has already been set as the primary key.");
                } else {
                    hasPrimary = true;
                }
            }
            return true;
        }
    }

    @Override
    public boolean remove(Object o) { return columns.remove(o); }

    @Override
    public boolean containsAll(Collection c) { return columns.containsAll(c); }

    @Override
    public boolean addAll(Collection<? extends Column> c) {
        boolean wasModified = false;
        for(Column col : c) {
            if(!columns.contains(col)) {
                wasModified = true;
                if(col.isPrimary()) {
                    if(hasPrimary) {
                        throw new IllegalArgumentException("Attempted to add more than 1 column set as primary key!");
                    } else {
                        hasPrimary = true;
                    }
                }
                add(col);
            }
        }
        return wasModified;
    }

    @Override
    public boolean retainAll(Collection c) { return columns.retainAll(c); }

    @Override
    public boolean removeAll(Collection c) { return columns.removeAll(c); }

    @Override
    public void clear() { columns.clear(); }
}
