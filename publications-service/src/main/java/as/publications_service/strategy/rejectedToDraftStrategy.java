package as.publications_service.strategy;

import as.publications_service.entities.editorialStatus;
import as.publications_service.entities.digitalPublication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Estrategia: REJECTED → DRAFT
 * Permite al autor corregir una publicación rechazada devolviéndola a borrador.
 * Ejemplo: Si pub.status=REJECTED y newStatus=DRAFT,
 *          canTransition(...) true; executeTransition pone DRAFT.
 */
@Component
public class rejectedToDraftStrategy implements statusTransitionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(rejectedToDraftStrategy.class);

    @Override
    public boolean canTransition(digitalPublication pub, editorialStatus newStatus) {
        if (newStatus != editorialStatus.DRAFT) {
            return false;
        }

        if (pub.getStatus() != editorialStatus.REJECTED) {
            return false;
        }

        return true;
    }

    @Override
    public void executeTransition(digitalPublication pub, editorialStatus newStatus) {
        pub.setStatus(newStatus);
        logger.info("Publicación rechazada {} devuelta a borrador para correcciones", pub.getId());
    }

    @Override
    public String getDescription() {
        return "Transición de REJECTED a DRAFT (Ciclo de corrección)";
    }
}