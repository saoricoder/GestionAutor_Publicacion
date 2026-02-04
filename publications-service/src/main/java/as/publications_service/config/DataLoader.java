package as.publications_service.config;

import as.publications_service.entities.digitalPublication;
import as.publications_service.entities.editorialStatus;
import as.publications_service.repository.publicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * DataLoader - Carga datos iniciales en la base de datos
 */
@Configuration
public class DataLoader {
    
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Value("${AUTHORS_SERVICE_URL}")
    private String authorsServiceUrl;

    @Bean
    CommandLineRunner initDatabase(publicationRepository repository, RestTemplate restTemplate) {
        return args -> {
            logger.info("Limpiando publicaciones anteriores...");
            repository.deleteAll();
            
            logger.info("Cargando datos iniciales de publicaciones...");

            // Obtener los autores de authors-service
            List<String> authorIds = getAuthorIds(restTemplate);
            
            if (authorIds.isEmpty()) {
                logger.warn("No hay autores disponibles en authors-service");
                return;
            }

                // Publicación 1 - DRAFT
                digitalPublication pub1 = new digitalPublication();
                pub1.setTitle("Introducción a Microservicios con Spring Boot");
                pub1.setAuthorId(authorIds.get(0 % authorIds.size()));
                pub1.setContent("Los microservicios son un patrón arquitectónico que estructura una aplicación como un conjunto de servicios pequeños, autónomos y desplegables de forma independiente. Spring Boot facilita enormemente la creación de estos servicios proporcionando configuración automática y un servidor embebido.");
                pub1.setPublicationType("ARTICLE");
                pub1.setFormat("PDF");
                pub1.setFileSize(2.5);
                pub1.setStatus(editorialStatus.DRAFT);
                pub1.setCreatedDate(LocalDateTime.now().minusDays(10));
                repository.save(pub1);

                // Publicación 2 - IN_REVIEW
                digitalPublication pub2 = new digitalPublication();
                pub2.setTitle("Patrones de Diseño en Laravel: Repository Pattern");
                pub2.setAuthorId(authorIds.get(1 % authorIds.size()));
                pub2.setContent("El patrón Repository es una abstracción que permite separar la lógica de acceso a datos del resto de la aplicación. En Laravel, este patrón es especialmente útil para mantener el código limpio y testeable, facilitando el cambio de fuentes de datos sin afectar la lógica de negocio.");
                pub2.setPublicationType("TUTORIAL");
                pub2.setFormat("MARKDOWN");
                pub2.setFileSize(1.8);
                pub2.setStatus(editorialStatus.IN_REVIEW);
                pub2.setCreatedDate(LocalDateTime.now().minusDays(7));
                repository.save(pub2);

                // Publicación 3 - APPROVED
                digitalPublication pub3 = new digitalPublication();
                pub3.setTitle("React Hooks: Una Guía Completa");
                pub3.setAuthorId(authorIds.get(2 % authorIds.size()));
                pub3.setContent("Los Hooks de React revolucionaron la forma en que escribimos componentes funcionales. useState, useEffect y otros hooks permiten manejar estado y efectos secundarios sin necesidad de componentes de clase, resultando en código más limpio y reutilizable.");
                pub3.setPublicationType("GUIDE");
                pub3.setFormat("HTML");
                pub3.setFileSize(3.2);
                pub3.setStatus(editorialStatus.APPROVED);
                pub3.setCreatedDate(LocalDateTime.now().minusDays(5));
                repository.save(pub3);

                // Publicación 4 - PUBLISHED
                digitalPublication pub4 = new digitalPublication();
                pub4.setTitle("PostgreSQL vs MySQL: Comparativa Técnica");
                pub4.setAuthorId(authorIds.get(3 % authorIds.size()));
                pub4.setContent("PostgreSQL y MySQL son dos de los sistemas de gestión de bases de datos relacionales más populares. PostgreSQL destaca por su cumplimiento estricto de estándares SQL y características avanzadas como JSONB, mientras que MySQL es conocido por su velocidad y facilidad de uso en aplicaciones web.");
                pub4.setPublicationType("ARTICLE");
                pub4.setFormat("PDF");
                pub4.setFileSize(4.1);
                pub4.setStatus(editorialStatus.PUBLISHED);
                pub4.setCreatedDate(LocalDateTime.now().minusDays(3));
                repository.save(pub4);

                // Publicación 5 - PUBLISHED
                digitalPublication pub5 = new digitalPublication();
                pub5.setTitle("Docker para Desarrolladores: Guía Práctica");
                pub5.setAuthorId(authorIds.get(4 % authorIds.size()));
                pub5.setContent("Docker ha transformado la forma en que desplegamos aplicaciones. Con contenedores, podemos empaquetar aplicaciones junto con todas sus dependencias, garantizando que funcionen de manera idéntica en cualquier entorno, desde desarrollo hasta producción.");
                pub5.setPublicationType("TUTORIAL");
                pub5.setFormat("MARKDOWN");
                pub5.setFileSize(2.9);
                pub5.setStatus(editorialStatus.PUBLISHED);
                pub5.setCreatedDate(LocalDateTime.now().minusDays(1));
                repository.save(pub5);

                logger.info("✅ Se cargaron {} publicaciones de prueba", repository.count());
        };
    }
    
    private List<String> getAuthorIds(RestTemplate restTemplate) {
        try {
            logger.info("Consultando autores en: {}", authorsServiceUrl);
            Map<?, ?> response = restTemplate.getForObject(authorsServiceUrl + "/authors", Map.class);
            
            if (response != null && response.containsKey("data")) {
                Object data = response.get("data");
                if (!(data instanceof List)) return Collections.emptyList();

                List<Map<String, Object>> authors = (List<Map<String, Object>>) data;
                List<String> ids = new ArrayList<>();
                
                for (Map<String, Object> author : authors) {
                    Object id = author.get("id") != null ? author.get("id") : author.get("idAuthor");
                    if (id != null) {
                        ids.add(id.toString());
                    }
                }
                logger.info("✅ Se obtuvieron {} IDs de autores para seeding", ids.size());
                return ids;
            }
        } catch (Exception e) {
            logger.error("❌ Fallo al conectar con Authors Service: {}. El seeding se omitirá.", e.getMessage());
        }
        return Collections.emptyList();
    }
}
