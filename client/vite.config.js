import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import Sitemap from 'vite-plugin-sitemap' // 1. Import the plugin

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    // 2. Add the Sitemap configuration here
    Sitemap({ 
      hostname: 'https://floodimpact.online',
      dynamicRoutes: ['/dashboard', '/admin', '/report'] 
    }),
  ],
})