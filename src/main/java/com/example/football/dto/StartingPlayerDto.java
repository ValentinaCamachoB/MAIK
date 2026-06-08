package com.example.football.dto;

import lombok.Data;

@Data
public class StartingPlayerDto {

    /**
     * Nombre del jugador titular
     */
    private String playerName;

    /**
     * Puntaje final
     */
    private Double score;
}
