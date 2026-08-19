// src/main.jsx
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import { App } from "./App"; // Importación con llaves: exportación nombrada

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <App />
  </StrictMode>
);
