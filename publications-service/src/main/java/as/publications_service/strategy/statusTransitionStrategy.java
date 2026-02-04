package as.publications_service.strategy;

import as.publications_service.entities.editorialStatus;
import as.publications_service.entities.digitalPublication;

/**
 * Patrón Strategy: Define estrategias para transiciones de estado editorial.
 * Dónde: en publicaciones-service para cambiar estados (DRAFT, REVIEW, APPROVED...).
 * Por qué: aísla reglas por transición, permite agregar/modificar estrategias sin
 * tocar el núcleo; mejora mantenibilidad y pruebas de reglas específicas.
 */
public interface statusTransitionStrategy {
  
    /**
     * Valida si la transición de estado es permitida
     */
    boolean canTransition(digitalPublication pub, editorialStatus newStatus);
    
    /**
     * Ejecuta la transición de estado
     */
    void executeTransition(digitalPublication pub, editorialStatus newStatus);
    
    /**
     * Obtiene mensaje descriptivo de la estrategia
     */
    String getDescription();
}