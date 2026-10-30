package com.example.tienda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.tienda.dto.CategoryDTO;
import com.example.tienda.dto.CategoryRequestDTO;
import com.example.tienda.dto.HttpGlobalResponseDTO;
import com.example.tienda.entity.Categories;
import com.example.tienda.entity.Products;
import com.example.tienda.repository.CategoriesRepository;
import com.example.tienda.repository.ProductsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    /**
     * Repositorio de categorías
     */
    private final CategoriesRepository categoriesRepository;

    /**
     * Repositorio de productos
     */
    private final ProductsRepository productsRepository;

    public HttpGlobalResponseDTO<List<CategoryDTO>> getCategories() {
        HttpGlobalResponseDTO<List<CategoryDTO>> response = new HttpGlobalResponseDTO<>();
        List<CategoryDTO> listCategories = new ArrayList<>();

        List<Categories> categoriesFound = categoriesRepository.findAll();

        for (Categories c : categoriesFound) {
            CategoryDTO category = new CategoryDTO();
            category.setId(c.getId());
            category.setName(c.getName());

            listCategories.add(category);
        }

        response.setMessage("Categories found");
        response.setData(listCategories);

        return response;
    }

    public HttpGlobalResponseDTO<CategoryDTO> createCategory(CategoryRequestDTO request) {
        HttpGlobalResponseDTO<CategoryDTO> response = new HttpGlobalResponseDTO<>();
        CategoryDTO categoryDTO = new CategoryDTO();

        Optional<Categories> nameFound = categoriesRepository.findByName(request.getName());

        if (nameFound.isPresent()) {
            throw new IllegalStateException("Category name is already in use");
        }

        Categories category = new Categories();
        category.setName(request.getName());

        categoriesRepository.save(category);

        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        response.setMessage("Category created successfully");
        response.setData(categoryDTO);

        return response;
    }

    public HttpGlobalResponseDTO<String> deleteCategory(Long id) {
        HttpGlobalResponseDTO<String> response = new HttpGlobalResponseDTO<>();
        Optional<Categories> categoryFound = categoriesRepository.findById(id);

        if (categoryFound.isEmpty()) {
            throw new NoSuchElementException("Category not found");
        }

        List<Products> productsFound = productsRepository.findAll();

        for (Products p : productsFound) {
            if (p.getCategory().getId().equals(id)) {
                throw new IllegalStateException("Category has products");
            }
        }

        categoriesRepository.deleteById(id);

        response.setMessage("Category deleted successfully");

        return response;
    }
}

