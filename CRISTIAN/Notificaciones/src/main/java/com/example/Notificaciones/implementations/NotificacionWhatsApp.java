package com.example.Notificaciones.implementations;

import com.example.Notificaciones.interfaces.Notificacion;

public class NotificacionWhatsApp implements Notificacion {

    @Override
    public String enviar() {
        return "Notificacion enviada por WhatsApp";
    }

}