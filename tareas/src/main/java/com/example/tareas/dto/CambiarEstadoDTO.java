package com.example.tareas.dto;

import com.example.tareas.model.EstadoTarea;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambiarEstadoDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoTarea estado;
}
