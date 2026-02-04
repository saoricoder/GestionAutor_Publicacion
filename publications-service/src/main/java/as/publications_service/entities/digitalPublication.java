package as.publications_service.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "publications")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class digitalPublication extends basePublication {
    @Column(length = 255, nullable = false)
    private String format; // PDF, PUB
    @Column(length = 255, nullable = false)
    private Double fileSize;


}