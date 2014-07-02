package us.drome.cobrasqlib;

public class Column {
    private String name;
    private DataType dataType;
    private boolean isPrimary;
    
    public Column(String name, DataType dataType, boolean isPrimary) {
        this.name = name;
        this.dataType = dataType;
        this.isPrimary = isPrimary;
    }
    
    public String getName() { return this.name; }
    
    public DataType getType() { return this.dataType; }
    
    public boolean isPrimary() { return this.isPrimary; }
}
