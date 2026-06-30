package com.example.tareas.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tareas.dto.TareaRequestDTO;
import com.example.tareas.dto.TareaResponseDTO;
import com.example.tareas.exception.TareaNotFoundException;
import com.example.tareas.exception.TransicionEstadoInvalidaException;
import com.example.tareas.model.EstadoTarea;
import com.example.tareas.model.PrioridadTarea;
import com.example.tareas.model.Tarea;
import com.example.tareas.repository.TareaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;

    private static final List<String> CAMPOS_VALIDOS_ORDEN =
            Arrays.asList("fechaCreacion", "prioridad", "titulo");

    public List<TareaResponseDTO> listarTareas(String ordenarPor) {
        if (ordenarPor == null || ordenarPor.isBlank()) {
            ordenarPor = "fechaCreacion";
        }

        if (!CAMPOS_VALIDOS_ORDEN.contains(ordenarPor)) {
            throw new IllegalArgumentException(
                "Campo de ordenamiento invalido. Valores permitidos: fechaCreacion, prioridad, titulo");
        }

        Sort sort = Sort.by(Sort.Direction.ASC, ordenarPor);
        List<Tarea> tareas = tareaRepository.findAll(sort);

        return convertirAListaDTO(tareas);
    }

    public TareaResponseDTO buscarPorId(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new TareaNotFoundException(
                        "No se encontro la tarea con id: " + id));
        return convertirADTO(tarea);
    }

    public TareaResponseDTO crear(TareaRequestDTO request) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setPrioridad(request.getPrioridad());

        if (request.getEstado() != null) {
            tarea.setEstado(request.getEstado());
        } else {
            tarea.setEstado(EstadoTarea.PENDIENTE);
        }

        Tarea guardada = tareaRepository.save(tarea);
        return convertirADTO(guardada);
    }


    public TareaResponseDTO actualizar(Long id, TareaRequestDTO request) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new TareaNotFoundException(
                        "No se encontro la tarea con id: " + id));

        if (request.getEstado() != null && request.getEstado() != tarea.getEstado()) {
            validarTransicionEstado(tarea.getEstado(), request.getEstado());
            tarea.setEstado(request.getEstado());
        }

        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setPrioridad(request.getPrioridad());

        Tarea actualizada = tareaRepository.save(tarea);
        return convertirADTO(actualizada);
    }

    public TareaResponseDTO cambiarEstado(Long id, EstadoTarea nuevoEstado) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new TareaNotFoundException(
                        "No se encontro la tarea con id: " + id));

        validarTransicionEstado(tarea.getEstado(), nuevoEstado);
        tarea.setEstado(nuevoEstado);

        Tarea actualizada = tareaRepository.save(tarea);
        return convertirADTO(actualizada);
    }

    public void eliminar(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new TareaNotFoundException("No se encontro la tarea con id: " + id);
        }
        tareaRepository.deleteById(id);
    }

    public List<TareaResponseDTO> filtrarPorEstado(EstadoTarea estado) {
        List<Tarea> tareas = tareaRepository.findByEstado(estado);
        return convertirAListaDTO(tareas);
    }

    public List<TareaResponseDTO> filtrarPorPrioridad(PrioridadTarea prioridad) {
        List<Tarea> tareas = tareaRepository.findByPrioridad(prioridad);
        return convertirAListaDTO(tareas);
    }

    public List<TareaResponseDTO> buscarPorTitulo(String texto) {
        List<Tarea> tareas = tareaRepository.findByTituloContainingIgnoreCase(texto);
        return convertirAListaDTO(tareas);
    }

    private void validarTransicionEstado(EstadoTarea actual, EstadoTarea nuevo) {
        if (actual == EstadoTarea.CANCELADA) {
            throw new TransicionEstadoInvalidaException(
                "Una tarea CANCELADA no puede cambiar de estado");
        }

        if (actual == EstadoTarea.COMPLETADA &&
            (nuevo == EstadoTarea.PENDIENTE || nuevo == EstadoTarea.EN_PROGRESO)) {
            throw new TransicionEstadoInvalidaException(
                "Una tarea COMPLETADA no puede volver a " + nuevo);
        }
    }

    private TareaResponseDTO convertirADTO(Tarea tarea) {
        TareaResponseDTO dto = new TareaResponseDTO();
        dto.setId(tarea.getId());
        dto.setTitulo(tarea.getTitulo());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setEstado(tarea.getEstado());
        dto.setPrioridad(tarea.getPrioridad());
        dto.setFechaCreacion(tarea.getFechaCreacion());
        dto.setFechaActualizacion(tarea.getFechaActualizacion());
        return dto;
    }

    private List<TareaResponseDTO> convertirAListaDTO(List<Tarea> tareas) {
        List<TareaResponseDTO> lista = new ArrayList<>();
        for (Tarea tarea : tareas) {
            lista.add(convertirADTO(tarea));
        }
        return lista;
    }
}
