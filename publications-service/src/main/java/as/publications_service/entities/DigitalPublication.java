package as.publications_service.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "publications")
@Getter
@Setter
public class DigitalPublication extends BasePublication {
    private Long authorId; // Referencia al ID del Microservicio 1 [cite: 42]

    @Override
    public String getPublicationType() {
        return "DIGITAL_CONTENT";
    }
}