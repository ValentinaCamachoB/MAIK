package com.carvajal.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carvajal.wishlist.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}

