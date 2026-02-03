# Publications Service - Docker

Guía rápida de Docker para el microservicio de Publicaciones.

### Puertos y variables
- Puerto por defecto (host): `PUBLICATIONS_PORT` (8080)
- Variables de BD desde la raíz (`.env`): `POSTGRES_*`
- Variable para URL del Authors Service: `AUTHORS_SERVICE_URL` (se inyecta en `docker-compose.yml`)

### Ejecutar con Docker Compose (recomendado)
Desde la raíz:

```bash
cp .env.example .env
docker compose up -d --build
```

El servicio se inicia a partir de `publications-service/Dockerfile` (multi-stage). El JAR se construye con Maven en la etapa de build.

### Ejecutar sólo el servicio (opcional)

```bash
# Construir imagen
docker build -t publications-service:local ./publications-service

# Ejecutar (ejemplo)
docker run --rm -e SPRING_DATASOURCE_URL=jdbc:postgresql://db-publications:5432/db_publications -p 8080:8080 publications-service:local
```

### Notas
- Si el Authors Service no está disponible, la creación de publicaciones que requieren validación de autor fallará.
- Ajusta `application.yaml` o usa variables de entorno para la configuración de la BD.
