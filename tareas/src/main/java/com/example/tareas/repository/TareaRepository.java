package com.example.tareas.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tareas.model.EstadoTarea;
import com.example.tareas.model.PrioridadTarea;
import com.example.tareas.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByEstado(EstadoTarea estado);

    List<Tarea> findByPrioridad(PrioridadTarea prioridad);

    List<Tarea> findByTituloContainingIgnoreCase(String texto);

    List<Tarea> findAll(Sort sort);
}
