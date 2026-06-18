package com.carvajal.wishlist.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class HistoricoResponseDTO {

    private Long idHistorico;
    private Long idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private String accion;
    private LocalDateTime fechaAccion;
}
