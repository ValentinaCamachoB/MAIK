import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { VitePWA } from "vite-plugin-pwa";

export default defineConfig({
  plugins: [
    react(),
    VitePWA({
      // "autoUpdate" refresca el Service Worker sin que el usuario
      // tenga que desinstalar/reinstalar la app
      registerType: "autoUpdate",

      // Archivos estáticos que queremos incluir en el caché
      includeAssets: ["icon-192.png", "icon-512.png"],

      // Manifest de la PWA, generado automáticamente por Vite durante el build
      manifest: {
        name: "Portafolio de Valentina Camacho",
        short_name: "Portafolio",
        description: "Landing page tipo portafolio construida con React y Vite",
        theme_color: "#0f172a",
        background_color: "#0f172a",
        display: "standalone", // oculta la barra del navegador al instalar
        start_url: "/",
        icons: [
          {
            src: "icon-192.png",
            sizes: "192x192",
            type: "image/png",
          },
          {
            src: "icon-512.png",
            sizes: "512x512",
            type: "image/png",
          },
        ],
      },
    }),
  ],
});
