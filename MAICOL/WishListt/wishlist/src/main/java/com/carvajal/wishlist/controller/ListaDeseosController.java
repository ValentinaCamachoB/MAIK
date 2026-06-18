package com.carvajal.wishlist.controller;

import java.util.List;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.carvajal.wishlist.dto.ActualizarDeseoRequestDTO;
import com.carvajal.wishlist.dto.AgregarDeseoRequestDTO;
import com.carvajal.wishlist.dto.HistoricoResponseDTO;
import com.carvajal.wishlist.dto.HttpGlobalResponse;
import com.carvajal.wishlist.dto.ItemDeseoResponseDTO;
import com.carvajal.wishlist.dto.MessageResponseDTO;
import com.carvajal.wishlist.service.ListaDeseosService;
 
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
 
@RestController
@RequestMapping("/lista-deseos")
@RequiredArgsConstructor
public class ListaDeseosController {
 
    private final ListaDeseosService listaDeseosService;
 
    /**
     * Listar la lista de deseos del usuario
     */
    @GetMapping("/{idUsuario}")
    public HttpGlobalResponse<List<ItemDeseoResponseDTO>> listarDeseos(@PathVariable Long idUsuario) {
        return listaDeseosService.listarDeseos(idUsuario);
    }
 
    /**
     * Agregar un producto a la lista de deseos
     */
    @PostMapping("/agregar")
    public ResponseEntity<MessageResponseDTO> agregarDeseo(
            @Valid @RequestBody AgregarDeseoRequestDTO request) {
        try {
            MessageResponseDTO response = listaDeseosService.agregarDeseo(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
          
            MessageResponseDTO error = new MessageResponseDTO();
            error.setMessage("Ocurrio un error al agregar el producto");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
 
    /**
     * Actualizar la cantidad de un item
     */
    @PutMapping("/actualizar/{idListaDeseos}")
    public MessageResponseDTO actualizarDeseo(
            @PathVariable Long idListaDeseos,
            @Valid @RequestBody ActualizarDeseoRequestDTO request) {
        return listaDeseosService.actualizarDeseo(idListaDeseos, request);
    }
 
    /**
     * Eliminar un item de la lista
     */
    @DeleteMapping("/eliminar/{idListaDeseos}")
    public MessageResponseDTO eliminarDeseo(@PathVariable Long idListaDeseos) {
        return listaDeseosService.eliminarDeseo(idListaDeseos);
    }
 
    /**
     * Obtener el historico completo del usuario
     */
    @GetMapping("/historico/{idUsuario}")
    public HttpGlobalResponse<List<HistoricoResponseDTO>> getHistorico(@PathVariable Long idUsuario) {
        return listaDeseosService.getHistorico(idUsuario);
    }
}