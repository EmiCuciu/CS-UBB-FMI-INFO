package triatlon.rest.services;

public class RestServiceException extends RuntimeException {
    public RestServiceException(Exception message) {
        super(message);
    }
}
