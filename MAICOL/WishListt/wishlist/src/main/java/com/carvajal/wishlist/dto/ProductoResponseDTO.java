package com.carvajal.wishlist.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductoResponseDTO {

    private Long idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String categoria;
}