package as.publications_service.entities;
// Estados editoriales mínimos sugeridos

/**•
 Flujo Feliz: DRAFT -> IN_REVIEW -> APPROVED -> PUBLISHED
 •
 Flujo Directo (existente)s: DRAFT -> IN_REVIEW -> PUBLISHED (gracias a reviewToPublishedStrategy)
 •
 Flujo de Rechazo: DRAFT -> IN_REVIEW -> REJECTED -> DRAFT (reinicio)
 * */
public enum editorialStatus {
    DRAFT, IN_REVIEW, APPROVED, PUBLISHED, REJECTED
}
