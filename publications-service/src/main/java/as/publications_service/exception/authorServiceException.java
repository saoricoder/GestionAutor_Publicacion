package as.publications_service.exception;

/**
 * Excepción personalizada para errores de comunicación con Authors Service
 */
public class authorServiceException extends RuntimeException {
    
    private String errorCode;
    
    public authorServiceException(String message) {
        super(message);
        this.errorCode = "AUTHOR_SERVICE_ERROR";
    }
    
    public authorServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "AUTHOR_SERVICE_ERROR";
    }
    
    public authorServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
