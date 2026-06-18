package com.carvajal.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carvajal.wishlist.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}