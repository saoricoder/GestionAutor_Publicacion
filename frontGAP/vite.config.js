import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,        // Permite que Vite sea visible fuera del contenedor
    port: 5173,        // El puerto que mapeaste en tu docker-compose
    watch: {
      usePolling: true, // Crucial para que los cambios se vean en Windows/WSL2
    },
  },
})