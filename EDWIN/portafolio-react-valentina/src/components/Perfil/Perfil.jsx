// src/components/Perfil/Perfil.jsx
import "./Perfil.css";

export const Perfil = (props) => {
  return (
    <section id="perfil" className="perfil">
      <img
        className="perfil__imagen"
        src={props.imagen}
        alt={`Foto de perfil de ${props.nombre}`}
      />
      <h2 className="perfil__nombre">{props.nombre}</h2>
      <p className="perfil__rol">{props.rol}</p>
    </section>
  );
};
