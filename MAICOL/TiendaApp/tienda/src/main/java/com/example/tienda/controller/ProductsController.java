package com.example.tienda.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tienda.dto.HttpGlobalResponseDTO;
import com.example.tienda.dto.ProductDTO;
import com.example.tienda.dto.ProductRequestDTO;
import com.example.tienda.enums.RoleEnum;
import com.example.tienda.security.RequiresRole;
import com.example.tienda.service.ProductsService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    /**
     * Servicio de productos
     */
    private final ProductsService productsService;

    @GetMapping
    public ResponseEntity<HttpGlobalResponseDTO<List<ProductDTO>>> getProducts(@RequestParam(required = false) Long categoryId) {
        HttpGlobalResponseDTO<List<ProductDTO>> response = new HttpGlobalResponseDTO<>();

        try {
            response = productsService.getProducts(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpGlobalResponseDTO<ProductDTO>> getProduct(@PathVariable Long id) {
        HttpGlobalResponseDTO<ProductDTO> response = new HttpGlobalResponseDTO<>();

        try {
            response = productsService.getProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchElementException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/mine")
    @RequiresRole({ RoleEnum.VENDEDOR })
    public ResponseEntity<HttpGlobalResponseDTO<List<ProductDTO>>> getMyProducts(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<ProductDTO>> response = new HttpGlobalResponseDTO<>();

        try {
            Long userId = (Long) request.getAttribute("userId");
            response = productsService.getMyProducts(userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping
    @RequiresRole({ RoleEnum.VENDEDOR })
    public ResponseEntity<HttpGlobalResponseDTO<ProductDTO>> createProduct(@RequestBody ProductRequestDTO body, HttpServletRequest request) {
        HttpGlobalResponseDTO<ProductDTO> response = new HttpGlobalResponseDTO<>();

        try {
            Long userId = (Long) request.getAttribute("userId");
            response = productsService.createProduct(body, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NoSuchElementException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    @RequiresRole({ RoleEnum.VENDEDOR, RoleEnum.ADMIN })
    public ResponseEntity<HttpGlobalResponseDTO<ProductDTO>> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO body, HttpServletRequest request) {
        HttpGlobalResponseDTO<ProductDTO> response = new HttpGlobalResponseDTO<>();

        try {
            Long userId = (Long) request.getAttribute("userId");
            Long rolId = (Long) request.getAttribute("rolId");
            response = productsService.updateProduct(id, body, userId, rolId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchElementException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (SecurityException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @RequiresRole({ RoleEnum.VENDEDOR, RoleEnum.ADMIN })
    public ResponseEntity<HttpGlobalResponseDTO<String>> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        HttpGlobalResponseDTO<String> response = new HttpGlobalResponseDTO<>();

        try {
            Long userId = (Long) request.getAttribute("userId");
            Long rolId = (Long) request.getAttribute("rolId");
            response = productsService.deleteProduct(id, userId, rolId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchElementException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (SecurityException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
