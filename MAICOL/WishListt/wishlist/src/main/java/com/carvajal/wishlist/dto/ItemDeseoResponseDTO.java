package com.carvajal.wishlist.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ItemDeseoResponseDTO {

    private Long idListaDeseos;
    private Long idProducto;
    private String nombreProducto;
    private String descripcion;
    private BigDecimal precio;
    private Integer stockActual;
    private Integer cantidadDeseada;
    private LocalDateTime fechaAgregado;
    private Boolean sinStock;
    private String mensajeNotificacion;
}
