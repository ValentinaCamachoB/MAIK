// src/components/Proyectos/Proyectos.jsx
import { TarjetaProyecto } from "./TarjetaProyecto";
import "./Proyectos.css";

export const Proyectos = () => {
  return (
    <section id="proyectos" className="proyectos">
      <h2>Proyectos</h2>
      <div className="proyectos__grid">
        {/* Cada TarjetaProyecto es la MISMA función-componente,
            reutilizada con datos diferentes vía props */}
        <TarjetaProyecto
          imagen="/proyecto-1.jpg"
          titulo="App de Recetas"
          descripcion="Aplicación web para descubrir y guardar recetas favoritas."
        />
        <TarjetaProyecto
          imagen="/proyecto-2.jpg"
          titulo="Gestor de Tareas"
          descripcion="Organizador de tareas diarias con categorías y prioridades."
        />
      </div>
    </section>
  );
};