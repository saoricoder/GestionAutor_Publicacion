package as.publications_service.strategy;
    import as.publications_service.entities.editorialStatus;
    import as.publications_service.entities.digitalPublication;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.stereotype.Component;
    
    /**
     * Estrategia: DRAFT → IN_REVIEW
     * Validaciones: La publicación debe tener contenido válido
     * Ejemplo: Si pub.status=DRAFT y tiene título y contenido no vacíos,
     *          canTransition(...) devuelve true y executeTransition pone IN_REVIEW.
     */
    @Component
public class draftToReviewStrategy implements statusTransitionStrategy {
     
        private static final Logger logger = LoggerFactory.getLogger(draftToReviewStrategy.class);
        
        @Override
        public boolean canTransition(digitalPublication pub, editorialStatus newStatus) {
            if (newStatus != editorialStatus.IN_REVIEW) {
                return false;
            }
            
            // Validaciones específicas
            if (pub.getContent() == null || pub.getContent().trim().isEmpty()) {
                logger.warn("No se puede pasar a IN_REVIEW: contenido vacío");
                return false;
            }
            
            if (pub.getTitle() == null || pub.getTitle().trim().isEmpty()) {
                logger.warn("No se puede pasar a IN_REVIEW: título vacío");
                return false;
            }
            
            return pub.getStatus() == editorialStatus.DRAFT;
        }
        
        @Override
        public void executeTransition(digitalPublication pub, editorialStatus newStatus) {
            pub.setStatus(newStatus);
            logger.info("Publicación {} movida a IN_REVIEW", pub.getId());
        }
        
        @Override
        public String getDescription() {
            return "Transición de DRAFT a IN_REVIEW con validaciones de contenido";
        }
    }