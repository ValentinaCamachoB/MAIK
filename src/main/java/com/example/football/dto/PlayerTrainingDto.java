package com.example.football.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayerTrainingDto {

    /**
     * Nombre del jugador
     */
    @NotBlank(message = "El nombre del jugador es obligatorio")
    private String playerName;

    /**
     * Potencia de tiro 
     */
    @NotNull(message = "La potencia de tiro es obligatoria")
    @Min(value = 0, message = "La potencia de tiro no puede ser negativa")
    private Double shotPower;

    /**
     * Velocidad del jugador en
     */
    @NotNull(message = "La velocidad es obligatoria")
    @Min(value = 0, message = "La velocidad no puede ser negativa")
    private Double speed;

    /**
     * Cantidad de pases efectivos
     */
    @NotNull(message = "Los pases efectivos son obligatorios")
    @Min(value = 0, message = "Los pases efectivos no pueden ser negativos")
    private Integer effectivePasses;
}

