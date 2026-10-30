package com.example.tienda.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tienda.dto.CategoryDTO;
import com.example.tienda.dto.CategoryRequestDTO;
import com.example.tienda.dto.HttpGlobalResponseDTO;
import com.example.tienda.enums.RoleEnum;
import com.example.tienda.security.RequiresRole;
import com.example.tienda.service.CategoriesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {

    /**
     * Servicio de categorías
     */
    private final CategoriesService categoriesService;

    @GetMapping
    @RequiresRole({ RoleEnum.ADMIN, RoleEnum.CLIENTE, RoleEnum.VENDEDOR })
    public ResponseEntity<HttpGlobalResponseDTO<List<CategoryDTO>>> getCategories() {
        HttpGlobalResponseDTO<List<CategoryDTO>> response = new HttpGlobalResponseDTO<>();

        try {
            response = categoriesService.getCategories();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping
    @RequiresRole({ RoleEnum.ADMIN })
    public ResponseEntity<HttpGlobalResponseDTO<CategoryDTO>> createCategory(@RequestBody CategoryRequestDTO request) {
        HttpGlobalResponseDTO<CategoryDTO> response = new HttpGlobalResponseDTO<>();

        try {
            response = categoriesService.createCategory(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalStateException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @RequiresRole({ RoleEnum.ADMIN })
    public ResponseEntity<HttpGlobalResponseDTO<String>> deleteCategory(@PathVariable Long id) {
        HttpGlobalResponseDTO<String> response = new HttpGlobalResponseDTO<>();

        try {
            response = categoriesService.deleteCategory(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchElementException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalStateException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

