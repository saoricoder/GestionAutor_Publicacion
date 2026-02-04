package as.publications_service.strategy;
    import as.publications_service.entities.editorialStatus;
    import as.publications_service.entities.digitalPublication;
    import as.publications_service.exception.invalidStatusTransitionException;
    import org.springframework.stereotype.Component;
    import java.util.List;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    
    /**
     * Patrón Facade/Coordinator: Orquesta las diferentes estrategias de transición.
     * Dónde: en publicaciones-service, punto único para aplicar cambios de estado.
     * Por qué: centraliza selección/ejecución de Strategy, evita lógica dispersa,
     * mejora trazabilidad (logging) y control de errores de transiciones.
     */
    @Component
public class statusTransitionManager {

        private static final Logger logger = LoggerFactory.getLogger(statusTransitionManager.class);
        private final List<statusTransitionStrategy> strategies;
        
        public statusTransitionManager(List<statusTransitionStrategy> strategies) {
            this.strategies = strategies;
        }
        
        public void transitionStatus(digitalPublication pub, editorialStatus newStatus) {
            logger.info("Intentando transición de {} a {}", pub.getStatus(), newStatus);
            
            for (statusTransitionStrategy strategy : strategies) {
                if (strategy.canTransition(pub, newStatus)) {
                    strategy.executeTransition(pub, newStatus);
                    logger.info("Transición exitosa usando estrategia: {}", strategy.getDescription());
                    return;
                }
            }
            
            throw new invalidStatusTransitionException(
                "No se puede pasar de " + pub.getStatus() + " a " + newStatus
            );
        }
    }