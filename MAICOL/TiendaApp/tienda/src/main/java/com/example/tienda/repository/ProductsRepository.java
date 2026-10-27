package com.example.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Long> {

}
