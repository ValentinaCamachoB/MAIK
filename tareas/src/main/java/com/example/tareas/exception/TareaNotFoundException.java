package com.example.tareas.exception;

public class TareaNotFoundException extends RuntimeException {

    public TareaNotFoundException(String mensaje) {
        super(mensaje);
    }
}
