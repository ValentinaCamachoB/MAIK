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
          imagen="/proyecto-agronet.jpg"
          titulo="AgroNet"
          descripcion="Proyecto colaborativo con Spring Boot y Angular. Desarrollé el módulo de recuperación de contraseña: expiración de token, formulario de restablecimiento y registro de eventos de seguridad."
        />
        <TarjetaProyecto
          imagen="/proyecto-supermarket.jpg"
          titulo="SuperMarket"
          descripcion="Sistema de gestión de supermercado construido en equipo con Spring Boot y MySQL, con módulos de productos, proveedores, empleados y ventas."
        />
        <TarjetaProyecto
          imagen="/proyecto-pokeapi.jpg"
          titulo="Pokedex Angular"
          descripcion="Aplicación Angular que consume la PokeAPI, con búsqueda reactiva usando RxJS (switchMap, forkJoin, debounceTime)."
        />
      </div>
    </section>
  );
};
