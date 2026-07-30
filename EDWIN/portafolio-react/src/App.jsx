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
        nombre="Valentina Camacho"
        rol="Aprendiz SENA"
      />
      <Introduccion
        texto="Soy aprendiz de desarrollo y analisis de software"
      />
      <Proyectos />
      <Estudios />
      <Footer />
    </div>
  );
};