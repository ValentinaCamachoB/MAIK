package com.example.tienda.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductRequestDTO {
    /**
     * Nombre del producto
     */
    private String name;

    /**
     * Descripción del producto
     */
    private String description;

    /**
     * Precio del producto
     */
    private BigDecimal price;

    /**
     * Cantidad disponible
     */
    private Integer stock;

    /**
     * Categoría del producto
     */
    private Long categoryId;
}
