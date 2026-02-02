package as.publications_service.strategy;

import as.publications_service.entities.editorialStatus;
import as.publications_service.entities.digitalPublication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Estrategia: IN_REVIEW → PUBLISHED
 * Validaciones: La publicación debe estar en revisión
 */
@Component
public class reviewToPublishedStrategy implements statusTransitionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(reviewToPublishedStrategy.class);

    @Override
    public boolean canTransition(digitalPublication pub, editorialStatus newStatus) {
        // Solo maneja transiciones hacia PUBLISHED
        if (newStatus != editorialStatus.PUBLISHED) {
            return false;
        }

        // Solo permite pasar desde IN_REVIEW
        if (pub.getStatus() != editorialStatus.IN_REVIEW) {
            logger.warn("No se puede publicar: el estado actual no es IN_REVIEW (es {})", pub.getStatus());
            return false;
        }

        return true;
    }

    @Override
    public void executeTransition(digitalPublication pub, editorialStatus newStatus) {
        pub.setStatus(newStatus);
        logger.info("Publicación {} publicada exitosamente", pub.getId());
    }

    @Override
    public String getDescription() {
        return "Transición de IN_REVIEW a PUBLISHED";
    }
}