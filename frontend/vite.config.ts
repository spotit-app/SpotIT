import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import EnvironmentPlugin from 'vite-plugin-environment';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), EnvironmentPlugin('all')],
  server: {
    proxy: {
      '/api': {
        // TODO: update API_URL
        target: 'API_URL',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
});
