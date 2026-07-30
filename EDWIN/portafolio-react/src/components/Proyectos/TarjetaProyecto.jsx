// src/components/Proyectos/TarjetaProyecto.jsx
export const TarjetaProyecto = (props) => {
  return (
    <article className="tarjeta-proyecto">
      <img
        className="tarjeta-proyecto__imagen"
        src={props.imagen}
        alt={props.titulo}
      />
      <h3 className="tarjeta-proyecto__titulo">{props.titulo}</h3>
      <p className="tarjeta-proyecto__descripcion">{props.descripcion}</p>
    </article>
  );
};