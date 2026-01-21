package as.publications_service.repository;

import as.publications_service.entities.BasePublication;
import as.publications_service.entities.DigitalPublication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepository extends JpaRepository<DigitalPublication, Long> {
}
