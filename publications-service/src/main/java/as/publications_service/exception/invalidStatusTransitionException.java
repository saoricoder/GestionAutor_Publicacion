package as.publications_service.exception;
    /**
     * Excepción para transiciones de estado no permitidas
     */
public class invalidStatusTransitionException extends RuntimeException {


        
        public invalidStatusTransitionException(String message) {
            super(message);
        }
        
        public invalidStatusTransitionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

