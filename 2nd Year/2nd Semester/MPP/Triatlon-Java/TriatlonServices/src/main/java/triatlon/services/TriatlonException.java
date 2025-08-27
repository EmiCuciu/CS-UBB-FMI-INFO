package triatlon.services;

public class TriatlonException extends Exception{
    public TriatlonException() {
    }

    public TriatlonException(String message) {
        super(message);
    }

    public TriatlonException(String message, Throwable cause) {
        super(message, cause);
    }
}