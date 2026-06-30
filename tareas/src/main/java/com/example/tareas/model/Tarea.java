package com.example.tareas.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tareas")
@Data
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTarea estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", nullable = false)
    private PrioridadTarea prioridad;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    /**
     * Se ejecuta UNA SOLA VEZ, justo antes de insertar la tarea en la BD.
     * Asigna la fecha de creacion y de actualizacion automaticamente.
     */
    @PrePersist
    public void asignarFechaCreacion() {
        LocalDateTime ahora = LocalDateTime.now();
        this.fechaCreacion = ahora;
        this.fechaActualizacion = ahora;
        // Si no se envio estado, se asigna PENDIENTE por defecto
        if (this.estado == null) {
            this.estado = EstadoTarea.PENDIENTE;
        }
    }

    /**
     * Se ejecuta CADA VEZ antes de actualizar la tarea.
     * Actualiza la fecha de actualizacion automaticamente.
     */
    @PreUpdate
    public void actualizarFechaActualizacion() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
