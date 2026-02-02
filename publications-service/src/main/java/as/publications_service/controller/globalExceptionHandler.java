package as.publications_service.controller;
    import as.publications_service.exception.ResourceNotFoundException;
    import as.publications_service.exception.authorServiceException;
    import as.publications_service.exception.invalidStatusTransitionException;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RestControllerAdvice;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import java.util.HashMap;
    import java.util.Map;
    
    /**
     * Manejo global de excepciones para errores consistentes
     */
    @RestControllerAdvice
public class globalExceptionHandler {

        
        private static final Logger logger = LoggerFactory.getLogger(globalExceptionHandler.class);
        
        @ExceptionHandler(authorServiceException.class)
        public ResponseEntity<Map<String, Object>> handleAuthorServiceException(authorServiceException e) {
            logger.error("Error en servicio de autores: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("errorCode", e.getErrorCode());
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
        
        @ExceptionHandler(invalidStatusTransitionException.class)
        public ResponseEntity<Map<String, Object>> handleInvalidStatusTransition(invalidStatusTransitionException e) {
            logger.error("Transición inválida: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("errorCode", "INVALID_STATUS_TRANSITION");
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
            logger.error("Recurso no encontrado: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("errorCode", "RESOURCE_NOT_FOUND");
            response.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
            logger.error("Error interno: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("errorCode", "INTERNAL_ERROR");
            // Incluimos el mensaje real de la excepción para facilitar el debug
            response.put("message", "Error interno del servidor: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    
}
