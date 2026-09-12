// src/app/index.tsx
import { useState } from "react";
import { FlatList, Text, View, StyleSheet, ActivityIndicator, Pressable, useWindowDimensions, TextInput } from "react-native";
import { Link } from "expo-router";
import { usePokemonList } from "../hooks/usePokemonList";
import { useFavorites } from "../context/FavoritesContext";

const HomeScreen = () => {
  const { pokemons, loading, error } = usePokemonList(20);
  const { width } = useWindowDimensions();
  const columnas = width > 600 ? 3 : width > 380 ? 2 : 1;
  const { isFavorite, toggleFavorite } = useFavorites();
  const [busqueda, setBusqueda] = useState<string>("");

  if (loading) {
    return (
      <View style={styles.center}>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  if (error) {
    return (
      <View style={styles.center}>
        <Text>Ocurrió un error: {error}</Text>
      </View>
    );
  }

  const pokemonsFiltrados = pokemons.filter((p) =>
    p.name.toLowerCase().includes(busqueda.toLowerCase())
  );

  return (
    <View style={styles.container}>
      <TextInput
        style={styles.buscador}
        placeholder="Buscar Pokémon..."
        value={busqueda}
        onChangeText={setBusqueda}
        autoCapitalize="none"
      />
      {pokemonsFiltrados.length === 0 ? (
        <View style={styles.center}>
          <Text>No se encontró ningún Pokémon.</Text>
        </View>
      ) : (
        <FlatList
          key={columnas}
          data={pokemonsFiltrados}
          numColumns={columnas}
          keyExtractor={(item) => item.name}
          contentContainerStyle={styles.list}
          renderItem={({ item }) => (
            <View style={styles.row}>
              <Link href={`/pokemon/${item.name}`} asChild>
                <Pressable style={{ flex: 1 }}>
                  <Text style={styles.name}>{item.name}</Text>
                </Pressable>
              </Link>
              <Pressable onPress={() => toggleFavorite(item.name)}>
                <Text style={styles.star}>{isFavorite(item.name) ? "★" : "☆"}</Text>
              </Pressable>
            </View>
          )}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1 },
  center: { flex: 1, justifyContent: "center", alignItems: "center" },
  buscador: {
    margin: 16,
    padding: 12,
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 8,
    fontSize: 16,
  },
  list: { padding: 16, paddingTop: 0 },
  row: {
    flex: 1,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingVertical: 12,
    paddingHorizontal: 8,
    borderBottomWidth: 1,
    borderColor: "#eee",
  },
  name: { fontSize: 18, textTransform: "capitalize" },
  star: { fontSize: 20, color: "#f5a623", paddingHorizontal: 8 },
});

export default HomeScreen;