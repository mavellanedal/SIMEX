# ==========================================
# ETAPA 1: Construcció del Frontend (Vue)
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
# ETAPA 3: Imatge Final de Producció (NGINX + PHP-FPM + SQL Server)
# ==========================================
# CAMBIO AQUÍ: Subimos a PHP 8.3 para que Microsoft SQL Server nos deje compilar
FROM php:8.3-fpm

# 1. Instal·lar NGINX, dependències de PHP i els DRIVERS de SQL SERVER
RUN apt-get update && apt-get install -y \
    nginx \
    libpng-dev libjpeg-dev libfreetype6-dev zip unzip git libonig-dev libxml2-dev \
    gnupg2 apt-transport-https curl unixodbc-dev \
    # A) Afegir repositori oficial de Microsoft
    && curl -fsSL https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor -o /usr/share/keyrings/microsoft-prod.gpg \
    && curl -fsSL https://packages.microsoft.com/config/debian/12/prod.list > /etc/apt/sources.list.d/mssql-release.list \
    && apt-get update \
    # B) Instal·lar el driver ODBC de Microsoft acceptant la llicència automàticament
    && ACCEPT_EULA=Y apt-get install -y msodbcsql18 \
    # C) Instal·lar extensions de PHP normals (GD, MySQL, etc.)
    && docker-php-ext-configure gd --with-freetype --with-jpeg \
    && docker-php-ext-install pdo_mysql mbstring exif pcntl bcmath gd xml \
    # D) Instal·lar les extensions de SQL Server via PECL
    && pecl install sqlsrv pdo_sqlsrv \
    && docker-php-ext-enable sqlsrv pdo_sqlsrv \
    # E) Netejar la cache per reduir el pes de la imatge
    && apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# 2. Configurar NGINX
RUN rm -f /etc/nginx/sites-enabled/default
COPY nginx.conf /etc/nginx/conf.d/default.conf

WORKDIR /var/www/html

# 3. Copiar el backend de l'Etapa 2
COPY --from=backend-builder /app /var/www/html

# 4. Copiar el frontend (Vue compilat) a la carpeta "public" de Laravel
COPY --from=frontend-builder /app/dist /var/www/html/public

# 5. Permisos estrictes per a NGINX i PHP-FPM (www-data)
RUN chown -R www-data:www-data /var/www/html/storage /var/www/html/bootstrap/cache

# Exposar el port 80
EXPOSE 80

# 6. Arrencar NGINX en segon pla i deixar PHP-FPM en primer pla
CMD service nginx start && php-fpm
