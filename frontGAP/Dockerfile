
FROM node:18-alpine

WORKDIR /app

# Instalar utilidades necesarias para el Healthcheck de Docker
RUN apk add --no-cache curl bash

# Copiar archivos de dependencias primero para aprovechar el caché de capas
COPY package*.json ./
RUN npm install --quiet

# Copiar el resto del código fuente
COPY . .

# Informar a Docker el puerto que utiliza Vite
EXPOSE 5173

# El comando crítico para Docker + WSL2: --host permite el acceso externo
CMD ["sh", "-c", "npm run dev -- --host 0.0.0.0"]