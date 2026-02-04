package as.publications_service.dtos;

import as.publications_service.entities.editorialStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateDTO {
    @NotNull
    @JsonProperty("status")
    private editorialStatus status;
}