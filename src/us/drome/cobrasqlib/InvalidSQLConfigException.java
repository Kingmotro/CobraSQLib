package us.drome.cobrasqlib;

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
