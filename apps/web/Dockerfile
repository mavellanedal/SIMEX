# ==========================================
# ETAPA 1: Construcción del Frontend (Vue)
# ==========================================
FROM node:20-alpine AS frontend-builder
WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# ==========================================
# ETAPA 2: Construcción del Backend (Laravel)
# ==========================================
FROM composer:2.7 AS backend-builder
WORKDIR /app
COPY backend/composer.json backend/composer.lock ./
RUN composer install --no-dev --ignore-platform-reqs --no-scripts --prefer-dist
COPY backend/ ./
RUN composer dump-autoload --optimize

# ==========================================
# ETAPA 3: Imagen final (Nginx + PHP-FPM + Supervisor)
# ==========================================
FROM php:8.3-fpm AS app

# Instala Nginx, supervisor y dependencias de PHP/SQL Server
RUN apt-get update && apt-get install -y \
    nginx supervisor \
    libpng-dev libjpeg-dev libfreetype6-dev zip unzip git libonig-dev libxml2-dev \
    gnupg2 apt-transport-https curl unixodbc-dev \
    && curl -fsSL https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor -o /usr/share/keyrings/microsoft-prod.gpg \
    && curl -fsSL https://packages.microsoft.com/config/debian/12/prod.list > /etc/apt/sources.list.d/mssql-release.list \
    && apt-get update \
    && ACCEPT_EULA=Y apt-get install -y msodbcsql18 \
    && docker-php-ext-configure gd --with-freetype --with-jpeg \
    && docker-php-ext-install pdo_mysql mbstring exif pcntl bcmath gd xml \
    && pecl install sqlsrv pdo_sqlsrv \
    && docker-php-ext-enable sqlsrv pdo_sqlsrv \
    && apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

WORKDIR /var/www/html

# Copia el código de Laravel
COPY --from=backend-builder /app /var/www/html
# Copia los archivos públicos de Laravel y luego sobrescribe con el frontend Vue compilado
COPY --from=frontend-builder /app/dist /var/www/html/public

# Configuración de Nginx y Supervisor
COPY nginx.conf /etc/nginx/sites-available/default
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Permisos
RUN chown -R www-data:www-data /var/www/html/storage /var/www/html/bootstrap/cache \
    && chmod -R 775 /var/www/html/storage /var/www/html/bootstrap/cache

EXPOSE 80

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
