package us.drome.cobrasqlib;

/**
 * A simple custom exception to throw when a configuration error is detected in the API.
 * 
 * @author TheAcademician
 * @since 0.1
 */
public class InvalidSQLConfigException extends Exception {
    public InvalidSQLConfigException(String message) {
        super(message);
    }
    
    public InvalidSQLConfigException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public InvalidSQLConfigException(Throwable throwable) {
        super(throwable);
    }
}
