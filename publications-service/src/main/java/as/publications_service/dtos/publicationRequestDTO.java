package as.publications_service.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class publicationRequestDTO {
    @NotNull
    @Size(max =50)
    private String title;
    @NotNull
    private String content;
    @NotBlank
    @Size(max = 20)
    private String status = "DRAFT";
    @NotNull
    @JsonProperty("authorId")
    @JsonAlias("idAuthor")
    private String authorId;
    
    @Size(max = 20)
    private String publicationType = "ARTICLE";
    
    private String format = "PDF";
    
    private Double fileSize = 1.0;
}