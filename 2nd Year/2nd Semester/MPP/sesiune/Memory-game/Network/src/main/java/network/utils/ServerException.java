package network.utils;

public class ServerException extends Exception{
    public ServerException() {}

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

