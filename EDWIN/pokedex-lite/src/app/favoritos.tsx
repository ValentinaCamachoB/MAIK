// src/app/favoritos.tsx
import { FlatList, Text, View, StyleSheet, Pressable, ActivityIndicator } from "react-native";
import { Link } from "expo-router";
import { useFavorites } from "../context/FavoritesContext";

const FavoritosScreen = () => {
  const { favorites, loading } = useFavorites();

  if (loading) {
    return (
      <View style={styles.center}>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  if (favorites.length === 0) {
    return (
      <View style={styles.center}>
        <Text>Todavía no marcaste ningún favorito.</Text>
      </View>
    );
  }

  return (
    <FlatList
      data={favorites}
      keyExtractor={(name) => name}
      contentContainerStyle={styles.list}
      renderItem={({ item }) => (
        <Link href={`/pokemon/${item}`} asChild>
          <Pressable style={styles.row}>
            <Text style={styles.name}>{item}</Text>
          </Pressable>
        </Link>
      )}
    />
  );
};

const styles = StyleSheet.create({
  center: { flex: 1, justifyContent: "center", alignItems: "center" },
  list: { padding: 16 },
  row: { paddingVertical: 12, borderBottomWidth: 1, borderColor: "#eee" },
  name: { fontSize: 18, textTransform: "capitalize" },
});

export default FavoritosScreen;