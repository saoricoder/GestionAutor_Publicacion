package as.publications_service.controller;

import as.publications_service.dtos.publicationRequestDTO;
import as.publications_service.dtos.publicationResponseDTO;
import as.publications_service.dtos.StatusUpdateDTO;
import as.publications_service.services.publicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publications")
@CrossOrigin(origins = "*")
public class publicationController {
    
    private static final Logger logger = LoggerFactory.getLogger(publicationController.class);
    private final publicationService service;
    
    public publicationController(publicationService service) {
        this.service = service;
    }
    
    /**
     * POST /api/publications - Crear publicación
     * Valida que el autor exista antes de crear
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPublication(
        @Valid @RequestBody publicationRequestDTO request
    ) {
        logger.info("POST /api/publications - Creando publicación");
        
        try {
            publicationResponseDTO publication = service.createPublication(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", publication);
            response.put("message", "Publicación creada exitosamente");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creando publicación: {}", e.getMessage());
            throw e; // Deja que el GlobalExceptionHandler lo maneje
        }
    }
    
    /**
     * GET /api/publications/{id} - Obtener publicación con datos del autor
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPublication(
        @PathVariable String id
    ) {
        logger.info("GET /api/publications/{} - Obteniendo publicación", id);
        
        try {
            publicationResponseDTO publication = service.getPublication(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", publication);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error obteniendo publicación {}: {}", id, e.getMessage());
            throw e;
        }
    }
    
    /**
     * GET /api/publications - Listar publicaciones con filtro opcional por autor
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listPublications(
        @RequestParam(required = false) String idAuthor
    ) {
        logger.info("GET /api/publications - Listando publicaciones");
        
        try {
            List<publicationResponseDTO> publications = service.listPublications(idAuthor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", publications);
            response.put("count", publications.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error listando publicaciones: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * PUT /api/publications/{id} - Actualizar datos de la publicación
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePublication(
        @PathVariable String id,
        @Valid @RequestBody publicationRequestDTO request
    ) {
        logger.info("PUT /api/publications/{} - Actualizando publicación", id);
        
        try {
            publicationResponseDTO publication = service.updatePublication(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", publication);
            response.put("message", "Publicación actualizada exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error actualizando publicación {}: {}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * PATCH /api/publications/{id}/status - Actualizar estado editorial
     * Usa Strategy Pattern para validar transiciones
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
        @PathVariable String id,
        @Valid @RequestBody StatusUpdateDTO request
    ) {
        logger.info("PATCH /api/publications/{}/status - Actualizando a {}", id, request.getNewStatus());
        
        try {
            publicationResponseDTO publication = service.updateStatus(id, request.getNewStatus());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", publication);
            response.put("message", "Estado actualizado exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error actualizando estado de publicación {}: {}", id, e.getMessage());
            throw e;
        }
    }
    
    /**
     * DELETE /api/publications/{id} - Eliminar publicación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePublication(
        @PathVariable String id
    ) {
        logger.info("DELETE /api/publications/{} - Eliminando publicación", id);
        
        try {
            service.deletePublication(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Publicación eliminada exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error eliminando publicación {}: {}", id, e.getMessage());
            throw e;
        }
    }
}