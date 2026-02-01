FROM php:8.2-fpm

# Instalar dependencias del sistema y extensiones de PHP para MySQL
RUN apt-get update && apt-get install -y \
    libpng-dev libjpeg-dev libfreetype6-dev zip git unzip && \
    docker-php-ext-install pdo_mysql gd

# Instalar Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

WORKDIR /var/www
COPY . .

# Instalar dependencias de Laravel
RUN composer install --no-interaction --optimize-autoloader --no-dev

# Permisos para Laravel
RUN chown -R www-data:www-data /var/www/storage /var/www/bootstrap/cache

EXPOSE 8000
CMD php artisan serve --host=0.0.0.0 --port=8000
