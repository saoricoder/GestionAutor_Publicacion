# Gestión de Autores y Publicaciones (GestionAut_Pub)

Universidad: Fuerzas Armadas ESPE
- Autor: 
* Betty Rodriguez
* Victor Villamarin


Este proyecto implementa un sistema de gestión editorial utilizando una arquitectura de microservicios. El ecosistema está compuesto por:

-   **Frontend**: Una aplicación en React (Vite) para la interfaz de usuario.
-   **Authors Service**: Un microservicio en Laravel (PHP) para gestionar autores.
-   **Publications Service**: Un microservicio en Spring Boot (Java) para gestionar publicaciones.
-   **Bases de Datos**: MySQL para autores y PostgreSQL para publicaciones.

Todo el entorno está orquestado con Docker y Docker Compose para facilitar su despliegue y desarrollo.

## Requisitos

-   Docker (versión 20 o superior)
-   Docker Compose (versión 2 o superior, incluido en Docker Desktop)

## Puesta en Marcha con Docker

Sigue estos pasos para levantar todo el ecosistema de desarrollo en tu máquina local.

### 1. Configurar Variables de Entorno

Primero, clona el repositorio y navega a la raíz del proyecto. Luego, copia el archivo de ejemplo `.env.example` para crear tu configuración local.

```bash
cp .env.example .env
```

Puedes editar el archivo `.env` si necesitas cambiar los puertos por defecto o las credenciales de las bases de datos.

### 2. Construir y Levantar los Contenedores

Ejecuta el siguiente comando para construir las imágenes de Docker y levantar todos los servicios en segundo plano.

```bash
docker compose up -d --build
```

Este comando hará lo siguiente:
-   Construirá las imágenes para el frontend, el servicio de autores y el de publicaciones.
-   Iniciará los contenedores para cada servicio y sus respectivas bases de datos.
-   Creará una red interna para que los servicios se comuniquen entre sí.

## Migraciones de Base de Datos (Authors Service)

El servicio de autores (Laravel) requiere que se ejecuten migraciones para crear la estructura de la base de datos.


### Ejecución Automática

El contenedor del `authors-service` está configurado para **intentar ejecutar las migraciones automáticamente** cada vez que se inicia. Por lo tanto, en la mayoría de los casos, no necesitas hacer nada más después de `docker compose up`.

### Ejecución Manual

Si necesitas aplicar nuevas migraciones o reiniciar la base de datos mientras los contenedores están en ejecución, puedes usar los siguientes comandos:

-   **Para ejecutar migraciones pendientes:**
    ```bash
    docker compose exec authors-service php artisan migrate
    ```

-   **Para revertir todas las migraciones y volver a ejecutarlas (elimina todos los datos):**
    ```bash
    docker compose exec authors-service php artisan migrate:refresh
    ```

-   **Para eliminar todas las tablas y poblarlas con datos de prueba (recomendado para desarrollo):**
    ```bash
    docker compose exec authors-service php artisan migrate:fresh --seed
    ```
> **Advertencia:** Los comandos `migrate:refresh` y `migrate:fresh` eliminarán permanentemente los datos de la base de datos de autores.

## Acceso a los Servicios

Una vez que los contenedores estén en ejecución, puedes acceder a los servicios en las siguientes URLs:

-   **Frontend**: `http://localhost:5173`
-   **Authors Service (API)**: `http://localhost:8000/api/authors`
-   **Publications Service (API)**: `http://localhost:8080/api/publications`
-   **Base de Datos (MySQL - Autores)**: Conexión en el puerto `3307`
-   **Base de Datos (PostgreSQL - Publicaciones)**: Conexión en el puerto `5433`

## Comandos Útiles de Docker

-   **Ver el estado de los servicios:**
    ```bash
    docker compose ps
    ```

-   **Ver los logs de un servicio en tiempo real (ej. frontend):**
    ```bash
    docker compose logs -f frontend
    ```

-   **Detener y eliminar los contenedores:**
    ```bash
    docker compose down
    ```

-   **Detener contenedores y eliminar los volúmenes de datos (¡se perderán los datos de las BD!):**
    ```bash
    docker compose down -v
    ```

## Estrategias de Estado (Publications Service)

- Patrón Strategy: reglas de transición editorial independientes y extensibles.
- Coordinación central: [statusTransitionManager.java](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/statusTransitionManager.java)

Transiciones implementadas:
- DRAFT → IN_REVIEW — [draftToReviewStrategy.java](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/draftToReviewStrategy.java)
  - Requiere título y contenido no vacíos.
- IN_REVIEW → APPROVED — [reviewToApprovedStrategy.java](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/reviewToApprovedStrategy.java)
  - Solo si el estado actual es IN_REVIEW.
- IN_REVIEW → REJECTED — [reviewToRejectedStrategy.java](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/reviewToRejectedStrategy.java)
  - Solo si el estado actual es IN_REVIEW.
- IN_REVIEW → PUBLISHED — [reviewToPublishedStrategy.java](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/reviewToPublishedStrategy.java)
  - Publica directamente desde revisión.
- APPROVED → PUBLISHED — [approvedToPublishedStrategy.java](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/approvedToPublishedStrategy.java)
  - Publica únicamente si fue previamente aprobado.
- REJECTED → DRAFT — [rejectedToDraftStrategy.java](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/rejectedToDraftStrategy.java)
  - Devuelve a borrador para correcciones.

## Ahora se exponda el checklist que cumple
                    
A continuación presento una evaluación completa, con evidencias del código y el cumplimiento de todos los criterios.

**Backend por Microservicios**

- Authors Service (API, ORM, capas) — Cumple
  - Rutas y controlador: [api.php](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/authors-service/routes/api.php#L14-L31), [AuthorController](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/authors-service/app/Http/Controllers/AuthorController.php#L13-L33)
  - Validación: [storeAuthorRequest](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/authors-service/app/Http/Requests/storeAuthorRequest.php#L12-L21)
  - ORM y repositorio: [AuthorRepository](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/authors-service/app/Repositories/Eloquent/AuthorRepository.php#L11-L21)
  - Servicio y DTO: [AuthorService](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/authors-service/app/Services/AuthorService.php#L13-L27), [AuthorDTO](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/authors-service/app/DTOs/AuthorDTO.php#L18-L27)

- Publications Service (API, estados, ORM, capas) — Cumple
  - Endpoints REST: [publicationController](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/controller/publicationController.java#L33-L51)
  - Estados editoriales y estrategia: [statusTransitionManager](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/statusTransitionManager.java#L23-L37), [statusTransitionStrategy](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/statusTransitionStrategy.java#L10-L20)
  - ORM JPA: [digitalPublication](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/entities/digitalPublication.java#L16-L19), [publicationRepository](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/repository/publicationRepository.java#L15-L17)
  - Validación y excepciones: [publicationRequestDTO](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/dtos/publicationRequestDTO.java#L15-L26), [globalExceptionHandler](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/controller/globalExceptionHandler.java#L23-L33)

**Clases Abstractas y Derivadas**

- Publications: abstracta [basePublication](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/entities/basePublication.java#L16) y derivada [digitalPublication](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/entities/digitalPublication.java#L15)
- Authors: abstracta [BaseAuthor](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/authors-service/app/Models/Domain/BaseAuthor.php#L22) y derivada [RegularAuthor](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/authors-service/app/Models/Domain/RegularAuthor.php#L14)

**Dependencia entre Microservicios**

- Validación de autor vía HTTP desde Publications — Cumple
  - Cliente HTTP: [authorClienteService](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/cliente/authorClienteService.java#L23-L38)
  - Uso en creación: [publicationServiceImpl.createPublication](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/services/impl/publicationServiceImpl.java#L51-L61)
  - Manejo de errores: [globalExceptionHandler.authorServiceException](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/controller/globalExceptionHandler.java#L23-L33)
  - Tolerancia en listados: ajustado para no romper si falla Authors, devolviendo author=null [getPublication](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/services/impl/publicationServiceImpl.java#L91-L95), [listPublications](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/services/impl/publicationServiceImpl.java#L105-L111)

**Patrones de Diseño y SOLID**

- Patrones (cumple con ≥3):
  - Adapter: [authorClienteService](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/cliente/authorClienteService.java#L12-L18)
  - Strategy: [statusTransitionStrategy](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/statusTransitionStrategy.java#L7-L9), [statusTransitionManager](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/statusTransitionManager.java#L11-L14)
  - Facade/Orquestación de casos de uso: [publicationServiceImpl](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/services/impl/publicationServiceImpl.java#L23-L28) y [statusTransitionManager](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/strategy/statusTransitionManager.java#L10-L14)
- SOLID y buenas prácticas — Cumple
  - SRP/Capas: Controladores solo orquestan, servicios contienen lógica ([publicationController](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/controller/publicationController.java))
  - DTOs: inputs/outputs explícitos ([publicationRequestDTO](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/dtos/publicationRequestDTO.java), [publicationResponseDTO](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/dtos/publicationResponseDTO.java))
  - Validación: anotaciones Jakarta en Java, FormRequest en Laravel ([storeAuthorRequest](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/authors-service/app/Http/Requests/storeAuthorRequest.php))
  - Excepciones y handler global: [globalExceptionHandler](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/controller/globalExceptionHandler.java)
  - Inversión de dependencias: JPA repository interface ([publicationRepository](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/publications-service/src/main/java/as/publications_service/repository/publicationRepository.java#L10-L15))

**Frontend**

- Pantallas mínimas — Cumple
  - Autores: [Authors.jsx](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/frontGAP/src/components/Authors.jsx)
  - Publicaciones: [Publications.jsx](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/frontGAP/src/components/Publications.jsx)
- Integración con APIs y cambio de estado — Cumple
  - Adaptador API: [api.js](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/frontGAP/src/services/api.js#L97)
  - Cambio de estado con PATCH: [Publications.jsx handleSave](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/frontGAP/src/components/Publications.jsx#L119-L139) usa `updateStatus` cuando el estado cambia

**Docker y Orquestación**

- Docker Compose funcional — Cumple
  - Servicios con BD separadas y healthchecks: [docker-compose.yml](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/docker-compose.yml#L3-L21), [db-publications](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/docker-compose.yml#L52-L68), [authors-service](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/docker-compose.yml#L23-L49), [publications-service](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/docker-compose.yml#L71-L89)
- Documentación clara — Cumple
  - Pasos, puertos y variables: [README.md](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/README.md#L17-L43) y [README Migraciones](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/README.md#L44-L70)

**Reglas y Consideraciones**

- Sin acoplamiento excesivo: cada servicio usa su propia BD; no hay acceso directo cruzado.
- Una BD por microservicio: MySQL (autores), PostgreSQL (publicaciones) — ver [docker-compose.yml](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/docker-compose.yml#L2-L21) y [#L51-L68](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/docker-compose.yml#L51-L68)
- No se duplica lógica: validaciones y flujos separados por servicio.
- Compose levanta desde cero — ver [README](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/README.md#L31-L43)


**Verificación rápida (comandos)**

- Probar login (POST):
  ```powershell
  Invoke-RestMethod -Uri "http://localhost:8000/api/login" `
    -Method POST -ContentType "application/json" `
    -Body '{"email":"admin@example.com","password":"password123"}'
  ```
  Existe la colección en Postman con todos los endpoints: [Author-Publications.postman_collection.json](file:///c:/ARQUITECTURA%20SOFTWARE/GestionAut_Pub/Author-Publications.postman_collection.json)
  
- Publicaciones (listar):
  ```powershell
  curl http://localhost:8080/api/publications
  ```


