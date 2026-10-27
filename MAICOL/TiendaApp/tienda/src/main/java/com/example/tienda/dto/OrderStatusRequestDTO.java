package com.example.tienda.dto;

import lombok.Data;

@Data
public class OrderStatusRequestDTO {
    /**
     * Nuevo estado: PENDIENTE, ENVIADO, ENTREGADO o CANCELADO
     */
    private String status;
}
