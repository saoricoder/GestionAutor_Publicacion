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
         * Patrón Adapter: Este componente adapta las llamadas HTTP hacia el Authors Service.
         * Dónde: en publicaciones-service para consultar autores vía REST.
         * Por qué: desacopla la infraestructura HTTP del dominio, evita acoplamiento a la BD
         * del otro servicio y facilita pruebas/mocks y configuración del endpoint por entorno.
         */

@Component
public class authorClienteService {

    private static final Logger logger = LoggerFactory.getLogger(authorClienteService.class);
    
    // Ajustamos la llave para que coincida con tu application.yml
    @org.springframework.beans.factory.annotation.Value("${AUTHORS_SERVICE_URL}")
    private String authorsServiceUrl;

    private final RestTemplate restTemplate;
    
    public authorClienteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public Map<String, Object> validateAuthorExists(String idAuthor) {
        try {
            logger.info("Validando existencia de autor: {}", idAuthor);
            
            // Construcción limpia: authorsServiceUrl ya trae el "/authors"
            String url = authorsServiceUrl + "/authors/" + idAuthor; 
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && (Boolean) response.getOrDefault("success", false)) {
                return (Map<String, Object>) response.get("data");
            }
            return null;
        } catch (RestClientException e) {
            logger.error("Error al conectar con Authors Service: {}", e.getMessage());
            throw new authorServiceException("No se pudo validar el autor.", e);
        }
    }
    
    public authorDTO getAuthorDetails(String idAuthor) {
        try {
            String url = authorsServiceUrl + "/authors/" + idAuthor;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && (Boolean) response.getOrDefault("success", false)) {
                Map<String, Object> data = (Map<String, Object>) response.get("data");
                return mapToAuthorDTO(data);
            }
            return null;
        } catch (RestClientException e) {
            throw new authorServiceException("No se pudieron obtener los detalles del autor.", e);
        }
    }
    
    private authorDTO mapToAuthorDTO(Map<String, Object> data) {
        return new authorDTO(
            (String) data.get("idAuthor"), // Mapeo correcto al campo de Laravel
            (String) data.get("name"),
            (String) data.get("email"),
            (String) data.get("bio"),
            (String) data.get("affiliation")
        );
    }
}
    
