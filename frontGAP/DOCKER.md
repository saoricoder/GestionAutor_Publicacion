# Frontend (frontGAP) - Docker

Este archivo explica cómo ejecutar el frontend dentro del ecosistema Docker.

### Puertos
- `FRONTEND_PORT` por defecto `5173`.

### Configuración
El `docker-compose.yml` inyecta las variables `VITE_AUTHORS_API_URL` y `VITE_PUBLICATIONS_API_URL` para que el dev server apunte a los servicios dentro de la red Docker.

### Ejecutar con Docker Compose
Desde la raíz del repo:

```bash
cp .env.example .env
docker compose up -d --build
```

Accede al frontend en `http://localhost:${FRONTEND_PORT:-5173}`.

### Notas
- En producción se recomienda generar la build (`npm run build`) y servirla con `nginx` o similar en lugar del dev server de Vite.
