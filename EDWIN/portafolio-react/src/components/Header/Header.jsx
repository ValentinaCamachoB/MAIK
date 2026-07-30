// src/components/Header/Header.jsx
import "./Header.css";
export const Header = () => {
  return (
    <header className="header">
      <h1 className="header__logo">Valentina Camacho Betancourt</h1>
      <nav className="header__nav">
        {/* Las anclas HTML nos permiten navegar sin ninguna librería de rutas */}
        <a href="#perfil">Perfil</a>
        <a href="#introduccion">Sobre mí</a>
        <a href="#proyectos">Proyectos</a>
        <a href="#estudios">Estudios</a>
      </nav>
    </header>
  );
};