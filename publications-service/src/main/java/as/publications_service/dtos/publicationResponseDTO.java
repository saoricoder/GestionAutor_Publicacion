package as.publications_service.dtos;
    import as.publications_service.entities.editorialStatus;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import java.time.LocalDateTime;
    
    /**
     * DTO para respuesta enriquecida de Publicación con datos del Autor
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
public class publicationResponseDTO {
 
        @JsonProperty("idPublication")
        private String idPublication;
        
        @JsonProperty("title")
        private String title;
        
        @JsonProperty("content")
        private String content;
        
        @JsonProperty("status")
        private editorialStatus status;
        
        @JsonProperty("createdDate")
        private LocalDateTime createdDate;
        
        @JsonProperty("author")
        private authorDTO author;
    
}
