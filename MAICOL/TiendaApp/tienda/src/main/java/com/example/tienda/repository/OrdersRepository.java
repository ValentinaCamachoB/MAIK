package com.example.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
