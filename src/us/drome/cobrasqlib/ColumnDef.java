package us.drome.cobrasqlib;

public class ColumnDef {
    public String name;
    public Type type;
    public int precision;
    public int scale;
    public boolean isPrimary = false;
    public boolean isAutoincrement = false;
    public boolean isNotNull = false;
    public boolean isUnsigned = false;
    
    public ColumnDef(String name, Type type, Options... options) {
        this(name, type, type.getDefaultPrecision(), type.getDefaultScale(), options);
    }
    
    public ColumnDef(String name, Type type, int precision, Options... options) {
        this(name, type, precision, type.getDefaultScale(), options);
    }
    
    public ColumnDef(String name, Type type, int precision, int scale, Options... options) {
        this.name = name;
        this.type = type;
        this.precision = precision;
        this.scale = scale;
        
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
}
