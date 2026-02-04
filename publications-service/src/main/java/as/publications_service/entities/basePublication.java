package as.publications_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class basePublication {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    
    @Column(nullable = false)
    private String authorId;

    @Column(length = 255, nullable = false)
    private String title;
    
    @Column(length = 1000000, nullable = false)
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private editorialStatus status;
    
    @Column(length = 255, nullable = true)
    private String publicationType;
    
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}