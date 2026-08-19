// src/App.jsx
import { Header } from "./components/Header/Header";
import { Perfil } from "./components/Perfil/Perfil";
import { Introduccion } from "./components/Introduccion/Introduccion";
import { Proyectos } from "./components/Proyectos/Proyectos";
import { Estudios } from "./components/Estudios/Estudios";
import { Footer } from "./components/Footer/Footer";
import "./App.css";

export const App = () => {
  return (
    <div className="app">
      <Header />
      <Perfil
        imagen="/perfil.jpg"
        nombre="Valentina Camacho Betancourt"
        rol="Aprendiz de Análisis y Desarrollo de Software"
      />
      <Introduccion
        texto="Soy aprendiz de Análisis y Desarrollo de Software en el SENA, en formación
        como desarrolladora full stack. Me gusta construir proyectos completos —desde el
        backend hasta la interfaz— aplicando lo que voy aprendiendo en clase, tanto de
        forma individual como en equipo."
      />
      <Proyectos />
      <Estudios />
      <Footer />
    </div>
  );
};
