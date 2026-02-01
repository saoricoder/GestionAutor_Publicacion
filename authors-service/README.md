
# Authors Service

Servicio de gestión de autores para el sistema de publicaciones editoriales.

## Requisitos Previos

- PHP 8.2 o superior
- Composer
- Node.js y npm (para Vite)
- Base de datos MySQL

## Instalación

### 1. Instalar dependencias de PHP
```bash
composer install
```

### 2. Instalar dependencias de Node.js
```bash
npm install
```

### 3. Configurar el archivo .env
```bash
cp .env.example .env
php artisan key:generate
```

Actualiza las credenciales de la base de datos en el archivo `.env`:
```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=db_authors
DB_USERNAME=root
DB_PASSWORD=
```

### 4. Ejecutar migraciones
```bash
php artisan migrate
```

### 5. Ejecutar seeders (opcional)
Para poblar la base de datos con datos de prueba:
```bash
php artisan db:seed
```

## Ejecutar el Proyecto

### En desarrollo

**Terminal 1 - Servidor Laravel:**
```bash
php artisan serve
```

El servidor estará disponible en `http://localhost:8000`

**Terminal 2 - Vite (compilación de assets):**
```bash
npm run dev
```

### En producción

**Compilar assets:**
```bash
npm run build
```

**Ejecutar servidor:**
```bash
php artisan serve
```

## API Endpoints

- `GET /api/authors` - Obtener todos los autores
- `POST /api/authors` - Crear un nuevo autor
- `GET /api/authors/{id}` - Obtener autor por ID
- `PUT /api/authors/{id}` - Actualizar autor
- `DELETE /api/authors/{id}` - Eliminar autor

## Estructura del Proyecto

```
├── app/
│   ├── DTOs/              # Data Transfer Objects
│   ├── Http/              # Controllers, Requests
│   ├── Models/            # Modelos Eloquent
│   ├── Repositories/      # Capa de repositorio
│   └── Services/          # Lógica de negocio
├── database/
│   ├── migrations/        # Migraciones de BD
│   ├── seeders/           # Seeders de BD
│   └── factories/         # Factories para testing
├── routes/
│   ├── api.php            # Rutas API
│   └── web.php            # Rutas web
└── tests/                 # Tests unitarios e integración
```

## Comandos Útiles

```bash
# Crear una migración
php artisan make:migration nombre_migracion

# Crear un modelo
php artisan make:model NombreModelo

# Crear un seeder
php artisan make:seeder NombreSeeder

# Crear una factory
php artisan make:factory NombreFactory

# Ejecutar tests
php artisan test

# Limpiar cachés
php artisan cache:clear
```

## Testing

Ejecutar los tests del proyecto:
```bash
php artisan test
```

Con cobertura:
```bash
php artisan test --coverage
```
