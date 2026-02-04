package as.publications_service.dtos;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    
    /**
     * DTO para representar datos del Autor desde Authors Service
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
public class authorDTO {
       
        
        @JsonProperty("idAuthor")
        private String idAuthor;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("email")
        private String email;
        
        @JsonProperty("bio")
        private String bio;
        
        @JsonProperty("affiliation")
        private String affiliation;
    }

