package us.drome.cobrasqlib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ColumnSet implements Set<Column>{
    private HashSet<Column> columns;
    private boolean hasPrimary;
    
    public ColumnSet(Collection<Column> columns) {
        this.columns = new HashSet<>();
        hasPrimary = false;
        this.columns.addAll(columns);
    }
    
    public ColumnSet() {
        columns = new HashSet<>();
        hasPrimary = false;
    }
    
    public boolean hasPrimary() { return hasPrimary; }
    
    public Collection<Column> getColumns() { return columns; }
    
    public Column getColumn(String name) {
        for(Column c : columns) {
           if(c.getName().equalsIgnoreCase(name)) {
               return c;
           } 
        }
        return null;
    }
    
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
                    throw new IllegalArgumentException("One column has already been set as the primary key.");
                } else {
                    hasPrimary = true;
                }
            }
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
            wasModified &= columns.contains(c);
            if(col.isPrimary()) {
                if(hasPrimary) {
                    throw new IllegalArgumentException("Attempted to add more than 1 column set as primary key!");
                } else {
                    hasPrimary = true;
                }
            }
            add(col);
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
