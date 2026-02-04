import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import Sitemap from 'vite-plugin-sitemap'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    Sitemap({ 
      hostname: 'https://floodimpact.online',
      dynamicRoutes: ['/dashboard', '/admin', '/report'] 
    }),
  ],
  // ADD THIS SECTION FOR LOCAL DEVELOPMENT
  server: {
    proxy: {
      '/api': {
        target: 'http://13.60.179.95:8081',
        changeOrigin: true,
        secure: false,
      },
    },
  },
})