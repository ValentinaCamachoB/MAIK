package com.example.Notificaciones.factory;

import com.example.Notificaciones.implementations.NotificacionEmail;
import com.example.Notificaciones.implementations.NotificacionSMS;
import com.example.Notificaciones.implementations.NotificacionWhatsApp;
import com.example.Notificaciones.interfaces.Notificacion;

public class NotificacionFactory {

    public static Notificacion crearNotificacion(String tipo) {

        if (tipo.equalsIgnoreCase("EMAIL")) {
            return new NotificacionEmail();
        }

        if (tipo.equalsIgnoreCase("SMS")) {
            return new NotificacionSMS();
        }

        if (tipo.equalsIgnoreCase("WHATSAPP")) {
            return new NotificacionWhatsApp();
        }

        throw new IllegalArgumentException("Tipo de notificacion no valido: " + tipo);
    }

}
