package com.example.Notificaciones.service;

import com.example.Notificaciones.factory.NotificacionFactory;
import com.example.Notificaciones.interfaces.Notificacion;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    public String enviarNotificacion(String tipo) {
        Notificacion notificacion = NotificacionFactory.crearNotificacion(tipo);
        return notificacion.enviar();
    }

}
