# Authors Service - Docker

Este archivo explica cómo ejecutar el Authors Service dentro del ecosistema con Docker.

### Puertos y variables relevantes
- Puerto por defecto (host): `AUTHORS_PORT` (8000)
- Variables de conexión a BD gestionadas desde la raíz (`.env`): `MYSQL_*` (ver `.env.example`)

### Ejecutar con Docker Compose (recomendado)
Desde la raíz del repositorio:

```bash
cp .env.example .env
docker compose up -d --build
```

El servicio `authors-service` se construye usando `authors-service/Dockerfile` y queda accesible en `http://localhost:${AUTHORS_PORT:-8000}`.

### Ejecutar sólo el servicio (opcional)

```bash
# Construir imagen
docker build -t authors-service:local ./authors-service

# Ejecutar (ejemplo, enlazando la db existente por nombre de red)
docker run --rm -e DB_HOST=db-authors -e DB_DATABASE=db_authors -e DB_USERNAME=root -e DB_PASSWORD=root -p 8000:8000 authors-service:local
```

### Notas
- Durante el `CMD` se ejecuta `php artisan migrate --force || true` para intentar migraciones al arrancar (si la DB no está lista la llamada puede fallar y continuar).
- Para producción considera usar PHP-FPM + nginx y evitar `php artisan serve`.
