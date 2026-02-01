# Frontend (React + Vite)
FROM node:18-alpine
WORKDIR /app
# Añadir utilidades (curl para healthcheck)
RUN apk add --no-cache curl bash
COPY package*.json ./
RUN npm install --quiet
COPY . .
EXPOSE 5173
# Ejecuta Vite en modo dev accesible desde 0.0.0.0
CMD ["sh","-c","npm run dev -- --host 0.0.0.0"]
