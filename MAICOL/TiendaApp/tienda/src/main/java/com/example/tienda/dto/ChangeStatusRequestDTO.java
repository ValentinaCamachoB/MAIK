package com.example.tienda.dto;

import lombok.Data;

@Data
public class ChangeStatusRequestDTO {
    /**
     * true = activo, false = desactivado
     */
    private Boolean active;
}
