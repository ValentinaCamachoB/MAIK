package com.example.tienda.dto;

mport lombok.Data;

@Data
public class HttpGlobalResponseDTO<T> {
    /**
     * Mensaje de respuesta
     */
    private String message;

    /**
     * Objeto de respuesta
     */
    private T data;
}
