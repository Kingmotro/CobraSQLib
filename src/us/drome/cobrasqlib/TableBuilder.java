package us.drome.cobrasqlib;

public class TableBuilder {
        ColumnDef[] definitions;
        
        public TableBuilder(ColumnDef... definitions) throws InvalidSQLConfigException {
            this.definitions = definitions;
            for(ColumnDef def : definitions) {
                try {
                    def.type.verify(def.precision, def.scale, def.isPrimary, def.isAutoincrement, def.isNotNull, def.isUnsigned);
                } catch (InvalidSQLConfigException ex) {
                    throw new InvalidSQLConfigException("Invalid type configuration for column " + def.name + ": " + ex.getMessage());
                }
            }
        }
        
        public void testMethod() {
            
        }
    }