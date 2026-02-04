package as.publications_service.repository;

import as.publications_service.entities.digitalPublication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
/**
 *  Al ser una interfaz que extiende de JpaRepository, se aplica la inversión de dependencias,
 *  ya que el framework inyecta la implementación en tiempo de ejecución.
 */
@Repository
public interface publicationRepository extends JpaRepository<digitalPublication, UUID> {
    List<digitalPublication> findByAuthorId(String authorId);
}