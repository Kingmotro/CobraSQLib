package us.drome.cobrasqlib;

/**
 * An interface to provide for the specification of the various <tt>Column</tt> data types present across the different SQL implementations.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public interface DataType {

    /**
     * Returns a <tt>String</tt> representing the data type.
     * 
     * @return a <tt>String</tt> representing the data type.
     */
    public String getType();
    
    /**
     * Returns a <tt>Boolean</tt> indicating if this field can increment automatically.
     * 
     * @return <tt>true</tt> if the data type can increment; <tt>false</tt> if it cannot.
     */
    public Boolean canAutoincrement();
}
