package as.publications_service.strategy;

import as.publications_service.entities.editorialStatus;
import as.publications_service.entities.digitalPublication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Estrategia: IN_REVIEW → APPROVED
 * Validaciones: La publicación debe estar en revisión
 */
@Component
public class reviewToApprovedStrategy implements statusTransitionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(reviewToApprovedStrategy.class);

    @Override
    public boolean canTransition(digitalPublication pub, editorialStatus newStatus) {
        if (newStatus != editorialStatus.APPROVED) {
            return false;
        }

        if (pub.getStatus() != editorialStatus.IN_REVIEW) {
            logger.warn("No se puede aprobar: el estado actual no es IN_REVIEW (es {})", pub.getStatus());
            return false;
        }

        return true;
    }

    @Override
    public void executeTransition(digitalPublication pub, editorialStatus newStatus) {
        pub.setStatus(newStatus);
        logger.info("Publicación {} aprobada exitosamente", pub.getId());
    }

    @Override
    public String getDescription() {
        return "Transición de IN_REVIEW a APPROVED";
    }
}