package com.example.football.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainingRequestDto {
    
    /**
     * Numero entrenamiento
     */
    @NotNull(message = "El numero de entrenamiento es obligatorio")
    @Min(value = 1, message = "El numero de entrenamiento debe ser 1, 2 o 3")
    @Max(value = 3, message = "El numero de entrenamiento debe ser 1, 2 o 3")
    private Integer trainingNumber;
    
    /**
     *Lista de jugadores
     */
    @NotEmpty(message = "La lista de jugadores no puede estar vacia")
    @Valid
    private List<PlayerTrainingDto> players;
}
