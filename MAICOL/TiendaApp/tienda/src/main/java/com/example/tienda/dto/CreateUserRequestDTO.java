package com.example.tienda.dto;

import lombok.Data;

@Data
public class CreateUserRequestDTO {
    /**
     * Nombre de usuario
     */
    private String username;

    /**
     * Correo del usuario
     */
    private String email;

    /**
     * Contraseña del usuario
     */
    private String password;

    /**
     * Rol que tendrá el usuario (1=ADMIN, 2=CLIENTE, 3=VENDEDOR)
     */
    private Long rolId;
}
