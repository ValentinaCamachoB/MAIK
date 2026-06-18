package com.carvajal.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carvajal.wishlist.entity.HistoricoListaDeseos;

public interface HistoricoListaDeseosRepository extends JpaRepository<HistoricoListaDeseos, Long> {

    List<HistoricoListaDeseos> findByIdUsuarioOrderByFechaAccionDesc(Long idUsuario);
}
