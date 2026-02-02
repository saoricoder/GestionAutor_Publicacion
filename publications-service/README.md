# Microservicio de Publicaciones (Publications Service)

Este proyecto es un microservicio desarrollado en Spring Boot encargado de administrar publicaciones y su estado editorial.

## Requisitos Previos

*   **Java 17** o superior.
*   **Maven**.
*   **PostgreSQL** instalado y en ejecución.

## Configuración

### 1. Base de Datos

Debes crear una base de datos en PostgreSQL llamada `bd_publications`. Puedes hacerlo ejecutando el siguiente comando SQL en tu gestor de base de datos:

```sql
CREATE DATABASE bd_publications;
```

### 2. Credenciales de Base de Datos

Abre el archivo `src/main/resources/application.yaml` y actualiza las credenciales de la base de datos según tu configuración local (especialmente el usuario y la contraseña).

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bd_publications
    username: postgresql  # Cambia esto por tu usuario real (ej: postgres)
    password:            # Ingresa tu contraseña aquí si es necesaria
```

### 3. Dependencia Externa (Microservicio de Autores)

Para crear una publicación, este servicio valida que el autor exista consultando el **Microservicio de Autores**.

*   **Configuración actual:** Espera que el servicio de autores esté corriendo en `http://localhost:8000/api`.
*   Si el servicio de autores no está disponible, las peticiones `POST` fallarán.

## Ejecución

Para levantar el proyecto, abre una terminal en la raíz del directorio `publications-service` y ejecuta:

```bash
./mvnw spring-boot:run
```

El servicio iniciará en el puerto **8080**.

## API Endpoints

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/publications/` | Crear una nueva publicación (Requiere `authorId` válido en el servicio de autores). |
| `GET` | `/api/publications` | Listar todas las publicaciones. |
| `GET` | `/api/publications/{id}` | Obtener una publicación por su UUID. |
| `PATCH` | `/api/publications/{id}/status` | Actualizar el estado editorial (ej: `PUBLISHED`, `DRAFT`). |