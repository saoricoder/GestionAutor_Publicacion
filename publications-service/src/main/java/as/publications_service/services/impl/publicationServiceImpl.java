package as.publications_service.services.impl;

import as.publications_service.cliente.authorClienteService;
import as.publications_service.dtos.authorDTO;
import as.publications_service.dtos.publicationRequestDTO;
import as.publications_service.dtos.publicationResponseDTO;
import as.publications_service.entities.editorialStatus;
import as.publications_service.entities.digitalPublication;
import as.publications_service.exception.ResourceNotFoundException;
import as.publications_service.exception.authorServiceException;
import as.publications_service.exception.invalidStatusTransitionException;
import as.publications_service.repository.publicationRepository;
import as.publications_service.services.publicationService;
import as.publications_service.strategy.statusTransitionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * FACADE PATTERN: Orquesta la creación y gestión de publicaciones
 * Coordina entre Repository, AdapterClient y Strategy Manager
 */
@Service
public class publicationServiceImpl implements publicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(publicationServiceImpl.class);
    
    private final publicationRepository repository;
    private final authorClienteService authorServiceClient;
    private final statusTransitionManager statusTransitionManager;
    
    public publicationServiceImpl(
        publicationRepository repository,
        authorClienteService authorServiceClient,
        statusTransitionManager statusTransitionManager
    ) {
        this.repository = repository;
        this.authorServiceClient = authorServiceClient;
        this.statusTransitionManager = statusTransitionManager;
    }
    
    @Override
    @Transactional
    public publicationResponseDTO createPublication(publicationRequestDTO request) {
        logger.info("Creando publicación para autor: {}", request.getAuthorId());
        
        // VALIDACIÓN OBLIGATORIA: Verificar que el autor existe
        try {
            authorDTO author = authorServiceClient.getAuthorDetails(request.getAuthorId());
            if (author == null) {
                logger.error("Autor no encontrado: {}", request.getAuthorId());
                throw new authorServiceException(
                    "El autor con ID " + request.getAuthorId() + " no existe",
                    "AUTHOR_NOT_FOUND"
                );
            }
            
            // Crear publicación
            digitalPublication pub = new digitalPublication();
            // pub.setId() se genera automáticamente
            pub.setAuthorId(request.getAuthorId());
            pub.setTitle(request.getTitle());
            pub.setContent(request.getContent());
            pub.setStatus(editorialStatus.DRAFT);
            pub.setPublicationType(request.getPublicationType());
            pub.setFormat(request.getFormat());
            pub.setFileSize(request.getFileSize());
            
            digitalPublication saved = repository.save(pub);
            logger.info("Publicación creada: {}", saved.getId());
            
            return toResponseDTO(saved, author);
            
        } catch (authorServiceException e) {
            logger.error("Error validando autor: {}", e.getMessage());
            throw e;
        }
    }
    
    @Override
    public publicationResponseDTO getPublication(String idPublication) {
        logger.info("Obteniendo publicación: {}", idPublication);
        
        digitalPublication pub = repository.findById(UUID.fromString(idPublication))
            .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + idPublication));
        
        // Enriquecer con datos del autor
        authorDTO author = authorServiceClient.getAuthorDetails(pub.getAuthorId());
        
        return toResponseDTO(pub, author);
    }
    
    @Override
    public List<publicationResponseDTO> listPublications(String idAuthor) {
        logger.info("Listando publicaciones para autor: {}", idAuthor);
        
        List<digitalPublication> publications = idAuthor != null
            ? repository.findByAuthorId(idAuthor)
            : repository.findAll();
        
        return publications.stream()
            .map(pub -> {
                authorDTO author = authorServiceClient.getAuthorDetails(pub.getAuthorId());
                return toResponseDTO(pub, author);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public publicationResponseDTO updateStatus(String idPublication, editorialStatus newStatus) {
        logger.info("Actualizando estado de publicación: {} a {}", idPublication, newStatus);
        
        digitalPublication pub = repository.findById(UUID.fromString(idPublication))
            .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + idPublication));
        
        try {
            // STRATEGY PATTERN: Usar estrategia de transición adecuada
            statusTransitionManager.transitionStatus(pub, newStatus);
            digitalPublication updated = repository.save(pub);
            
            authorDTO author = authorServiceClient.getAuthorDetails(pub.getAuthorId());
            return toResponseDTO(updated, author);
            
        } catch (invalidStatusTransitionException e) {
            logger.error("Transición inválida: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public publicationResponseDTO updatePublication(String idPublication, publicationRequestDTO request) {
        logger.info("Actualizando publicación: {}", idPublication);

        digitalPublication pub = repository.findById(UUID.fromString(idPublication))
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + idPublication));

        // Actualizar campos permitidos
        pub.setTitle(request.getTitle());
        pub.setContent(request.getContent());
        pub.setPublicationType(request.getPublicationType());
        pub.setFormat(request.getFormat());
        pub.setFileSize(request.getFileSize());
        // No actualizamos el autor ni el estado aquí (el estado tiene su propio endpoint)

        digitalPublication updated = repository.save(pub);
        logger.info("Publicación actualizada: {}", updated.getId());

        authorDTO author = authorServiceClient.getAuthorDetails(pub.getAuthorId());
        return toResponseDTO(updated, author);
    }

    @Override
    @Transactional
    public void deletePublication(String idPublication) {
        logger.info("Eliminando publicación: {}", idPublication);
        
        digitalPublication pub = repository.findById(UUID.fromString(idPublication))
            .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + idPublication));
        
        repository.delete(pub);
        logger.info("Publicación eliminada: {}", idPublication);
    }
    
    private publicationResponseDTO toResponseDTO(digitalPublication pub, authorDTO author) {
        publicationResponseDTO dto = new publicationResponseDTO();
        dto.setIdPublication(pub.getId().toString());
        dto.setTitle(pub.getTitle());
        dto.setContent(pub.getContent());
        dto.setStatus(pub.getStatus());
        dto.setCreatedDate(pub.getCreatedDate());
        dto.setAuthor(author);
        return dto;
    }
}