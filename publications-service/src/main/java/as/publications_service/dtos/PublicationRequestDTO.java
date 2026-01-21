package as.publications_service.dtos;
import lombok.Data;

@Data
public class PublicationRequestDTO {
    private Long id;
    private String title;
    private String content;
    private String status;
    private Long authorId;
    private String publicationType;
}
