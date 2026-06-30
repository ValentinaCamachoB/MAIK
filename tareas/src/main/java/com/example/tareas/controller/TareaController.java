package com.example.tareas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tareas.dto.ApiResponse;
import com.example.tareas.dto.CambiarEstadoDTO;
import com.example.tareas.dto.TareaRequestDTO;
import com.example.tareas.dto.TareaResponseDTO;
import com.example.tareas.model.EstadoTarea;
import com.example.tareas.model.PrioridadTarea;
import com.example.tareas.service.TareaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TareaResponseDTO>>> listarTareas(
            @RequestParam(required = false) String ordenarPor) {

        List<TareaResponseDTO> tareas = tareaService.listarTareas(ordenarPor);
        ApiResponse<List<TareaResponseDTO>> respuesta =
                ApiResponse.exito("Tareas listadas correctamente", tareas);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TareaResponseDTO>> obtenerPorId(@PathVariable Long id) {
        TareaResponseDTO tarea = tareaService.buscarPorId(id);
        ApiResponse<TareaResponseDTO> respuesta =
                ApiResponse.exito("Tarea encontrada", tarea);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TareaResponseDTO>> crear(
            @Valid @RequestBody TareaRequestDTO request) {
        TareaResponseDTO creada = tareaService.crear(request);
        ApiResponse<TareaResponseDTO> respuesta =
                ApiResponse.exito("Tarea creada exitosamente", creada);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TareaResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TareaRequestDTO request) {
        TareaResponseDTO actualizada = tareaService.actualizar(id, request);
        ApiResponse<TareaResponseDTO> respuesta =
                ApiResponse.exito("Tarea actualizada correctamente", actualizada);
        return ResponseEntity.ok(respuesta);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<TareaResponseDTO>> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoDTO request) {
        TareaResponseDTO actualizada = tareaService.cambiarEstado(id, request.getEstado());
        ApiResponse<TareaResponseDTO> respuesta =
                ApiResponse.exito("Estado cambiado correctamente", actualizada);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        tareaService.eliminar(id);
        ApiResponse<Object> respuesta =
                ApiResponse.exito("Tarea eliminada correctamente", null);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/filtrar/estado")
    public ResponseEntity<ApiResponse<List<TareaResponseDTO>>> filtrarPorEstado(
            @RequestParam EstadoTarea estado) {
        List<TareaResponseDTO> tareas = tareaService.filtrarPorEstado(estado);
        ApiResponse<List<TareaResponseDTO>> respuesta =
                ApiResponse.exito("Tareas filtradas por estado: " + estado, tareas);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/filtrar/prioridad")
    public ResponseEntity<ApiResponse<List<TareaResponseDTO>>> filtrarPorPrioridad(
            @RequestParam PrioridadTarea prioridad) {
        List<TareaResponseDTO> tareas = tareaService.filtrarPorPrioridad(prioridad);
        ApiResponse<List<TareaResponseDTO>> respuesta =
                ApiResponse.exito("Tareas filtradas por prioridad: " + prioridad, tareas);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<TareaResponseDTO>>> buscarPorTitulo(
            @RequestParam String q) {
        List<TareaResponseDTO> tareas = tareaService.buscarPorTitulo(q);
        ApiResponse<List<TareaResponseDTO>> respuesta =
                ApiResponse.exito("Resultados de busqueda para: " + q, tareas);
        return ResponseEntity.ok(respuesta);
    }
}
