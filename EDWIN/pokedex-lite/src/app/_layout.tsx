// src/app/_layout.tsx
import { Tabs } from "expo-router";
import { FavoritesProvider } from "../context/FavoritesContext";

const RootLayout = () => {
  return (
    <FavoritesProvider>
      <Tabs>
        <Tabs.Screen
          name="index"
          options={{
            title: "Pokedex Lite",
            tabBarLabel: "Lista",
          }}
        />
        <Tabs.Screen
          name="favoritos"
          options={{
            title: "Favoritos",
            tabBarLabel: "★ Favoritos",
          }}
        />
        <Tabs.Screen
          name="pokemon/[name]"
          options={{
            title: "Detalle",
            href: null, // oculta esta ruta de la barra de pestañas, pero sigue siendo navegable
          }}
        />
      </Tabs>
    </FavoritesProvider>
  );
};

export default RootLayout;