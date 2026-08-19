# Portafolio de Valentina Camacho — React + PWA

Landing page tipo portafolio, construida siguiendo la guía *"De Cero a PWA"*
del Instructor Edwin Rozo: sin Hooks, sin React Router, componentes como
funciones flecha con exportación nombrada, y estructura de una carpeta por
componente (`.jsx` + `.css`).

## Antes de correrlo: agrega tus imágenes

Coloca estos archivos dentro de `public/` (no vienen incluidos en este entregable):

- `perfil.jpg` — tu foto de perfil
- `proyecto-agronet.jpg` — captura del proyecto AgroNet
- `proyecto-supermarket.jpg` — captura del proyecto SuperMarket
- `proyecto-pokeapi.jpg` — captura de la Pokedex Angular
- `icon-192.png` y `icon-512.png` — íconos cuadrados para la instalación como PWA

## Cómo correr el proyecto

```bash
npm install
npm run dev
```

Abre la URL que te muestre Vite (normalmente `http://localhost:5173`).

## Cómo probar la PWA (instalación + offline)

Los Service Workers solo se activan en la build de producción:

```bash
npm run build
npm run preview
```

Abre `http://localhost:4173`, revisa en DevTools → *Application* que el
manifest y el Service Worker estén activos, e instala la app desde el ícono
en la barra de direcciones.

## Estructura

```
src/
├── components/
│   ├── Header/
│   ├── Perfil/
│   ├── Introduccion/
│   ├── Proyectos/        (incluye TarjetaProyecto, reutilizado 3 veces)
│   ├── Estudios/
│   └── Footer/
├── App.jsx                orquesta las secciones y pasa las props
└── main.jsx                punto de entrada
```
