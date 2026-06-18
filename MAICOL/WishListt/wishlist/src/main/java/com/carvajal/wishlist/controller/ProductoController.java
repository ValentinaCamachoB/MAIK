package com.carvajal.wishlist.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carvajal.wishlist.dto.ProductoResponseDTO;
import com.carvajal.wishlist.service.ProductoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Obtiene el catalogo completo de productos
     */
    @GetMapping("/catalogo")
    public List<ProductoResponseDTO> getCatalogo() {
        return productoService.getCatalogo();
    }
}
