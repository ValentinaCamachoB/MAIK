package com.carvajal.wishlist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carvajal.wishlist.entity.ListaDeseos;

public interface ListaDeseosRepository extends JpaRepository<ListaDeseos, Long> {

    List<ListaDeseos> findByIdUsuario(Long idUsuario);

    Optional<ListaDeseos> findByIdUsuarioAndIdProducto(Long idUsuario, Long idProducto);
}
