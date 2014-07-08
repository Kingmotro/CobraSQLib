package us.drome.cobrasqlib;

import java.sql.Types;

public class TypeUtils {
    public static int getDefaultPrecision(Types type) {
        return 0;
    }
    
    public static boolean canAutoIncrement(Types type) {
        if(type.equals(Types.BIGINT) || type.equals(Types.INTEGER) || type.equals(Types.SMALLINT) || type.equals(Types.TINYINT)) {
            return true;
        } else {
            return false;
        }
    }
}
