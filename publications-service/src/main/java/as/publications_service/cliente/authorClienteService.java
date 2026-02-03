package as.publications_service.cliente;
    import as.publications_service.dtos.authorDTO;
    import as.publications_service.exception.authorServiceException;
    import org.springframework.stereotype.Component;
    import org.springframework.web.client.RestTemplate;
    import org.springframework.web.client.RestClientException;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import java.util.HashMap;
    import java.util.Map;

        /**
     * ADAPTER PATTERN: Adapta las llamadas HTTP hacia el Authors Service
     * Implementa la comunicación sincrónica entre microservicios
     */

@Component
public class authorClienteService {

        
        private static final Logger logger = LoggerFactory.getLogger(authorClienteService.class);
        @org.springframework.beans.factory.annotation.Value("${authors.service.url:http://localhost:8000/api/authors}")
        private String authorsServiceUrl;
        private static final long TIMEOUT_MS = 5000;
        
        private final RestTemplate restTemplate;
        
        public authorClienteService(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }
        
        /**
         * Valida que un autor existe en el Authors Service
         * @param idAuthor UUID del autor
         * @return Map con datos del autor o null si no existe
         */
        public Map<String, Object> validateAuthorExists(String idAuthor) {
            try {
                logger.info("Validando existencia de autor: {}", idAuthor);
                
                String url = authorsServiceUrl + "/" + idAuthor;
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                
                if (response != null && (Boolean) response.getOrDefault("success", false)) {
                    logger.info("Autor validado exitosamente: {}", idAuthor);
                    return (Map<String, Object>) response.get("data");
                }
                
                logger.warn("Autor no encontrado: {}", idAuthor);
                return null;
                
            } catch (RestClientException e) {
                logger.error("Error al conectar con Authors Service: {}", e.getMessage());
                throw new authorServiceException("No se pudo validar el autor. El servicio de autores no está disponible.", e);
            }
        }
        
        /**
         * Obtiene datos enriquecidos del autor
         */
        public authorDTO getAuthorDetails(String idAuthor) {
            try {
                String url = authorsServiceUrl + "/" + idAuthor;
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                
                if (response != null && (Boolean) response.getOrDefault("success", false)) {
                    Map<String, Object> data = (Map<String, Object>) response.get("data");
                    return mapToAuthorDTO(data);
                }
                
                return null;
                
            } catch (RestClientException e) {
                logger.error("Error obteniendo detalles del autor: {}", e.getMessage());
                throw new authorServiceException("No se pudieron obtener los detalles del autor.", e);
            }
        }
        
        private authorDTO mapToAuthorDTO(Map<String, Object> data) {
            return new authorDTO(
                (String) data.get("idAuthor"),
                (String) data.get("name"),
                (String) data.get("email"),
                (String) data.get("bio"),
                (String) data.get("affiliation")
            );
        }
    }

