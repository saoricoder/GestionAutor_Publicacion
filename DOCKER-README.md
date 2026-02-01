# Docker + Docker Compose — Despliegue completo

Este documento explica cómo ejecutar el ecosistema completo con Docker Compose: frontend, Authors Service (Laravel), Publications Service (Spring Boot) y las bases de datos.

## Qué contiene
- authors-service (Laravel)
- publications-service (Spring Boot)
- frontend (Vite + React)
- db-authors (MySQL)
- db-publications (PostgreSQL)

## Requisitos
- Docker (20+) instalado
- Docker Compose (v2 recommended) o Docker Desktop

## Archivos clave
- `docker-compose.yml` (en la raíz) — orquesta todo el stack
- `.env.example` — variables recomendadas (copiar a `.env` y editar)
- `authors-service/Dockerfile` — imagen para Laravel
- `publications-service/Dockerfile` — imagen multi-stage para Spring Boot
- `frontGAP/Dockerfile` — imagen para Vite (dev server)

## Variables de entorno
Copia `.env.example` a `.env` y ajusta si lo necesitas:

- MYSQL_*: configuración de la base de datos de Autores (MySQL)
- POSTGRES_*: configuración de la base de datos de Publicaciones (Postgres)
- AUTHORS_PORT, PUBLICATIONS_PORT, FRONTEND_PORT: puertos expuestos del host

## Puertos por defecto (host -> contenedor)
- Frontend: 5173 -> 5173
- Authors Service: 8000 -> 8000
- Publications Service: 8080 -> 8080
- MySQL (db-authors): 3307 -> 3306
- Postgres (db-publications): 5433 -> 5432

## Volúmenes
- `db_authors_data` - persistencia MySQL
- `db_publications_data` - persistencia Postgres

## Healthchecks
Cada servicio y base de datos incluye `healthcheck` para facilitar diagnósticos y permitir comprobaciones automáticas.

## Levantar el ecosistema (desarrollo)
Desde la raíz del repo:

1. Copiar variables de ejemplo:

```bash
cp .env.example .env
```

2. Construir e iniciar (en segundo plano):

```bash
docker compose up -d --build
```

3. Ver logs (ejemplo):

```bash
docker compose logs -f frontend
```

4. Ver estado de servicios y healthchecks:

```bash
docker compose ps
```

5. Para detener y eliminar contenedores (manteniendo volúmenes):

```bash
docker compose down
```

6. Para eliminar también volúmenes (¡WARNING! datos persistentes se borrarán):

```bash
docker compose down -v
```

## Notas y recomendaciones
- El `Publications Service` consulta al Authors Service para validar autores. Asegúrate que el Authors Service esté accesible en `http://authors-service:8000/api` (esto lo gestiona `docker-compose` internamente).
- Si necesitas que el frontend apunte a servicios externos en vez de los containers, modifica `VITE_AUTHORS_API_URL` y `VITE_PUBLICATIONS_API_URL` en `.env`.
- Para entornos de producción considerar:
  - Compilar la app React (`npm run build`) y servirla con nginx en lugar del dev server.
  - Desactivar el `php artisan serve` en favor de PHP-FPM + nginx.
  - Añadir copias de seguridad y políticas de restauración para volúmenes.

---

