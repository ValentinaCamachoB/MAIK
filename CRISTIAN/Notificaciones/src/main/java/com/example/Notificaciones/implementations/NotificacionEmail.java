package com.example.Notificaciones.implementations;

import com.example.Notificaciones.interfaces.Notificacion;

public class NotificacionEmail implements Notificacion {

    @Override
    public String enviar() {
        return "Notificacion enviada por correo electronico";
    }

}
