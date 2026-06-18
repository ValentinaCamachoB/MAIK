package com.carvajal.wishlist.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.carvajal.wishlist.dto.ProductoResponseDTO;
import com.carvajal.wishlist.entity.Producto;
import com.carvajal.wishlist.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<ProductoResponseDTO> getCatalogo() {
        List<ProductoResponseDTO> listaProductos = new ArrayList<>();
        List<Producto> productos = productoRepository.findAll();

        for (Producto producto : productos) {
            ProductoResponseDTO dto = new ProductoResponseDTO();
            dto.setIdProducto(producto.getIdProducto());
            dto.setNombre(producto.getNombre());
            dto.setDescripcion(producto.getDescripcion());
            dto.setPrecio(producto.getPrecio());
            dto.setStock(producto.getStock());
            dto.setCategoria(producto.getCategoria());
            listaProductos.add(dto);
        }

        return listaProductos;
    }
}
