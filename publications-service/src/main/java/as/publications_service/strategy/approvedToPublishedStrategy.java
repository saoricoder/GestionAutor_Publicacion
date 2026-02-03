package as.publications_service.strategy;

import as.publications_service.entities.editorialStatus;
import as.publications_service.entities.digitalPublication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Estrategia: APPROVED → PUBLISHED
 * Validaciones: La publicación debe estar aprobada previamente
 */
@Component
public class approvedToPublishedStrategy implements statusTransitionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(approvedToPublishedStrategy.class);

    @Override
    public boolean canTransition(digitalPublication pub, editorialStatus newStatus) {
        if (newStatus != editorialStatus.PUBLISHED) {
            return false;
        }

        // Solo permite publicar si ya fue aprobada (flujo estricto)
        // Opcional: Podrías permitir publicar directamente desde IN_REVIEW si esa es tu regla de negocio,
        // pero tener un paso de aprobación intermedio es común.
        if (pub.getStatus() != editorialStatus.APPROVED) {
            // Nota: reviewToPublishedStrategy ya maneja IN_REVIEW -> PUBLISHED si decides mantener ambos caminos.
            // Si quieres forzar el paso por APPROVED, deberías eliminar o modificar reviewToPublishedStrategy.
            return false;
        }

        return true;
    }

    @Override
    public void executeTransition(digitalPublication pub, editorialStatus newStatus) {
        pub.setStatus(newStatus);
        logger.info("Publicación aprobada {} ha sido publicada", pub.getId());
    }

    @Override
    public String getDescription() {
        return "Transición de APPROVED a PUBLISHED";
    }
}