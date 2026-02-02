package as.publications_service.strategy;

import as.publications_service.entities.editorialStatus;
import as.publications_service.entities.digitalPublication;

/**
 * STRATEGY PATTERN: Define estrategias para transiciones de estado editorial
 * Permite cambios de estado con validaciones específicas por tipo de transición
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