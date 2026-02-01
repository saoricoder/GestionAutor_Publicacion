# FrontGAP - Frontend Gestión de Autores y Publicaciones

Frontend de React con Vite para el sistema de gestión de autores y publicaciones editoriales (GestionAutores_Publicaciones).

## Requisitos Previos

- Node.js 18+ 
- npm

## Instalación

### 1. Instalar dependencias
```bash
npm install
```

### 2. Configurar variables de entorno

Crea un archivo `.env.local` en la raíz del proyecto:
```env
VITE_API_URL=http://localhost:8000/api
VITE_APP_NAME=GestionAut_Pub
```

## Ejecutar el Proyecto

### En desarrollo

**Terminal - Servidor de desarrollo con Hot Module Replacement (HMR):**
```bash
npm run dev
```

El frontend estará disponible en `http://localhost:5173`

### En producción

**Compilar assets:**
```bash
npm run build
```

**Previewear producción localmente:**
```bash
npm run preview
```

## Estructura del Proyecto

```
├── src/
│   ├── App.jsx              # Componente principal
│   ├── App.css              # Estilos principales
│   ├── main.jsx             # Punto de entrada
│   ├── index.css            # Estilos globales
│   ├── components/          # Componentes reutilizables
│   ├── services/            # Servicios API
│   └── assets/              # Imágenes y recursos
├── public/                  # Archivos estáticos
├── vite.config.js           # Configuración de Vite
├── eslint.config.js         # Configuración de ESLint
└── package.json             # Dependencias del proyecto
```

## Comandos Disponibles

```bash
# Instalar dependencias
npm install

# Ejecutar servidor de desarrollo
npm run dev

# Compilar para producción
npm run build

# Previewear build de producción
npm run preview

# Verificar linting
npm run lint
```

## Tecnologías Utilizadas

- **React** - Librería de UI
- **Vite** - Build tool rápido
- **ESLint** - Linter de código
- **Axios** - Cliente HTTP (si se configura)
- **React Router** - Enrutamiento (si se configura)

## Variables de Entorno

> 💡 Recomendado: define una variable por cada microservicio para mayor claridad.

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `VITE_AUTHORS_API_URL` | URL base del Authors Service | `http://localhost:8000/api` |
| `VITE_PUBLICATIONS_API_URL` | URL base del Publications Service | `http://localhost:8080/api` |
| `VITE_APP_NAME` | Nombre de la aplicación | `GestionAut_Pub` |

> Ejemplo `.env.local`:

```env
VITE_AUTHORS_API_URL=http://localhost:8000/api
VITE_PUBLICATIONS_API_URL=http://localhost:8080/api
VITE_APP_NAME=GestionAut_Pub
```

---

## Backends: Autores y Publicaciones 🔧

A continuación se resumen los **endpoints**, puertos y notas importantes para integrar el frontend con ambos microservicios.

### Authors Service (Laravel)
- Puerto por defecto: **8000** (ejecutable con `php artisan serve --port=8000`).
- Requisitos: PHP 8+, Composer, MySQL.
- Endpoints principales:
  - `GET  /api/authors` — Listar autores
  - `GET  /api/authors/{id}` — Obtener autor por UUID
  - `POST /api/authors` — Crear autor
  - `PUT  /api/authors/{id}` — Actualizar autor
  - `DELETE /api/authors/{id}` — Eliminar autor
- Notas: respuestas JSON con formato `{ success: true, data: ... }`.

### Publications Service (Spring Boot)
- Puerto por defecto: **8080** (`./mvnw spring-boot:run`).
- Requisitos: Java 17+, Maven, PostgreSQL.
- Debes crear la BD `bd_publications` y ajustar `src/main/resources/application.yaml` con tus credenciales.
- Endpoints principales:
  - `POST   /api/publications` — Crear publicación (requiere `authorId` válido en Authors Service)
  - `GET    /api/publications` — Listar publicaciones (opcional `?idAuthor=<uuid>`)
  - `GET    /api/publications/{id}` — Obtener publicación por UUID
  - `PUT    /api/publications/{id}` — Actualizar publicación
  - `PATCH  /api/publications/{id}/status` — Actualizar estado editorial
  - `DELETE /api/publications/{id}` — Eliminar publicación
- Notas: el servicio valida que el autor exista consultando `http://localhost:8000/api/authors` (configurable).

---

## Ejemplos de uso (curl) ✅

Obtener autores:
```bash
curl -s "$VITE_AUTHORS_API_URL/authors" | jq
```

Crear una publicación (ejemplo JSON):
```bash
curl -X POST "$VITE_PUBLICATIONS_API_URL/publications" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mi artículo",
    "content": "Contenido...",
    "authorId": "<uuid-author>",
    "status": "DRAFT"
  }'
```

Actualizar estado de publicación:
```bash
curl -X PATCH "$VITE_PUBLICATIONS_API_URL/publications/<id>/status" \
  -H "Content-Type: application/json" \
  -d '{"status":"PUBLISHED"}'
```

---

## Recomendaciones y Troubleshooting ⚠️

- Asegúrate de levantar primero el **Authors Service** si vas a crear publicaciones (Publications Service valida la existencia del autor).
- Si tienes errores CORS, revisa que el frontend use las URLs correctas en `.env.local` y que ambos backends permitan CORS (el Publications Service ya tiene `@CrossOrigin`).
- Si cambias puertos, actualiza las variables `VITE_AUTHORS_API_URL` y `VITE_PUBLICATIONS_API_URL`.

## Desarrollo

## Desarrollo

Durante el desarrollo, Vite proporciona:
- Hot Module Replacement (HMR) para recargas rápidas
- Fast Refresh para React
- Reconstrucción rápida de módulos

Los cambios se reflejarán automáticamente en el navegador sin necesidad de recargar.

## Troubleshooting

### Puerto 5173 ya está en uso
```bash
npm run dev -- --port 3000
```

### CORS errors
Verifica que la URL del backend en `.env.local` sea correcta y que el backend esté ejecutándose.

### Errores de ESLint
```bash
npm run lint -- --fix
```
