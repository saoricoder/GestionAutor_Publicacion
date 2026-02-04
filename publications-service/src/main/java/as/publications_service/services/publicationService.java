package as.publications_service.services;

import as.publications_service.dtos.publicationRequestDTO;
import as.publications_service.dtos.publicationResponseDTO;
import as.publications_service.entities.editorialStatus;

import java.util.List;

public interface publicationService {
    publicationResponseDTO createPublication(publicationRequestDTO request);
    publicationResponseDTO getPublication(String id);
    List<publicationResponseDTO> listPublications(String authorId);
    publicationResponseDTO updateStatus(String id, editorialStatus status);
    publicationResponseDTO updatePublication(String id, publicationRequestDTO request);
    void deletePublication(String id);
}