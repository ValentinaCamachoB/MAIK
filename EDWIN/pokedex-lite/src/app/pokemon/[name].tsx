// src/app/pokemon/[name].tsx
import { View, Text, Image, StyleSheet, ActivityIndicator, useWindowDimensions, Pressable } from "react-native";
import { useLocalSearchParams } from "expo-router";
import { usePokemonDetail } from "../../hooks/usePokemonDetail";
import { useFavorites } from "../../context/FavoritesContext";

const PokemonDetailScreen = () => {
  const { name } = useLocalSearchParams<{ name: string }>();
  const { pokemon, loading, error } = usePokemonDetail(name);
  const { width } = useWindowDimensions();
  const tamañoImagen = width > 600 ? 220 : 150;
  const { isFavorite, toggleFavorite } = useFavorites();

  if (loading) {
    return (
      <View style={styles.center}>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  if (error || !pokemon) {
    return (
      <View style={styles.center}>
        <Text>No se pudo cargar el Pokémon.</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      {pokemon.sprites.front_default && (
        <Image
          source={{ uri: pokemon.sprites.front_default }}
          style={{ width: tamañoImagen, height: tamañoImagen }}
        />
      )}
      <Text style={styles.title}>{pokemon.name}</Text>

      <Pressable onPress={() => toggleFavorite(pokemon.name)}>
        <Text style={styles.star}>
          {isFavorite(pokemon.name) ? "★ Quitar de favoritos" : "☆ Agregar a favoritos"}
        </Text>
      </Pressable>

      <Text>Tipos: {pokemon.types.map((t) => t.type.name).join(", ")}</Text>
      <Text>Altura: {pokemon.height}</Text>
      <Text>Peso: {pokemon.weight}</Text>
      <View style={styles.stats}>
        {pokemon.stats.map((s) => (
          <Text key={s.stat.name}>
            {s.stat.name}: {s.base_stat}
          </Text>
        ))}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  center: { flex: 1, justifyContent: "center", alignItems: "center" },
  container: { flex: 1, alignItems: "center", padding: 24 },
  title: { fontSize: 24, fontWeight: "bold", textTransform: "capitalize", marginVertical: 8 },
  star: { fontSize: 16, color: "#f5a623", marginBottom: 12 },
  stats: { marginTop: 16, alignItems: "flex-start" },
});

export default PokemonDetailScreen;