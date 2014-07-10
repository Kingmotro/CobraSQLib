package us.drome.cobrasqlib;

public class ColumnDef {
    public enum Options {
        isPrimary(true), isAutoincrement(true), isNotNull(false), isUnsigned(false);
        
        private boolean value;
        
        private Options(boolean value) {
            this.value = value;
        }
        
        public boolean getValue() { return value; }
    }
    
    
    public String name;
    public Type type;
    public int precision;
    public int scale;
    public boolean isPrimary;
    public boolean isAutoincrement;
    public boolean isNotNull;
    public boolean isUnsigned;
    
    public ColumnDef(String name, Type type) {
        this(name, type, type.getDefaultPrecision(), type.getDefaultScale(), false, false, true, false);
    }
    
    public ColumnDef(String name, Type type, boolean isPrimary) {
        this(name, type, type.getDefaultPrecision(), type.getDefaultScale(), true, false, false, false);
    }
    
    public ColumnDef(String name, Type type, int precision) {
        this(name, type, precision, type.getDefaultScale(), false, false, true, false);
    }
    
    public ColumnDef(String name, Type type, int precision, boolean isPrimary) {
        this(name, type, precision, type.getDefaultScale(), isPrimary, false, false, false);
    }
    
    public ColumnDef(String name, Type type, int precision, int scale) {
        this(name, type, precision, scale, false, false, true, false);
    }
    
    public ColumnDef(String name, Type type, int precision, int scale, boolean isPrimary) {
        this(name, type, precision, scale, isPrimary, false, false, false);
    }

    public ColumnDef(String name, Type type, int precision, int scale, boolean isPrimary, boolean isAutoincrement, boolean isNotNull, boolean isUnsigned) {
        this.name = name;
        this.type = type;
        this.precision = precision;
        this.scale = scale;
        this.isPrimary = isPrimary;
        this.isAutoincrement = isAutoincrement;
        this.isNotNull = isNotNull;
        this.isUnsigned = isUnsigned;
    }
    
    public ColumnDef(String name, Type type, int precision, int scale, Options... options) {
        for (Options opt : options) {
        }
    }
}
