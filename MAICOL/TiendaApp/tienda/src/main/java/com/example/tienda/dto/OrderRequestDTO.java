package com.example.tienda.dto;

import lombok.Data;

@Data
public class OrderRequestDTO {
    /**
     * Producto que se quiere comprar
     */
    private Long productId;

    /**
     * Cantidad que se quiere comprar
     */
    private Integer quantity;
}
