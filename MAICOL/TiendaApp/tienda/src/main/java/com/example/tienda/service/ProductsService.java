package com.example.tienda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.tienda.dto.HttpGlobalResponseDTO;
import com.example.tienda.dto.ProductDTO;
import com.example.tienda.dto.ProductRequestDTO;
import com.example.tienda.entity.Categories;
import com.example.tienda.entity.Products;
import com.example.tienda.entity.Users;
import com.example.tienda.enums.RoleEnum;
import com.example.tienda.repository.CategoriesRepository;
import com.example.tienda.repository.ProductsRepository;
import com.example.tienda.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsService {

    /**
     * Repositorio de productos
     */
    private final ProductsRepository productsRepository;

    /**
     * Repositorio de categorías
     */
    private final CategoriesRepository categoriesRepository;

    /**
     * Repositorio de usuarios
     */
    private final UsersRepository usersRepository;

    public HttpGlobalResponseDTO<List<ProductDTO>> getProducts(Long categoryId) {
        HttpGlobalResponseDTO<List<ProductDTO>> response = new HttpGlobalResponseDTO<>();
        List<ProductDTO> listProducts = new ArrayList<>();

        List<Products> productsFound = productsRepository.findAll();

        for (Products p : productsFound) {
            if (categoryId == null || p.getCategory().getId().equals(categoryId)) {
                ProductDTO product = new ProductDTO();
                product.setId(p.getId());
                product.setName(p.getName());
                product.setDescription(p.getDescription());
                product.setPrice(p.getPrice());
                product.setStock(p.getStock());
                product.setCategoryId(p.getCategory().getId());
                product.setCategoryName(p.getCategory().getName());
                product.setSellerId(p.getSeller().getId());
                product.setSellerUsername(p.getSeller().getUsername());

                listProducts.add(product);
            }
        }

        response.setMessage("Products found");
        response.setData(listProducts);

        return response;
    }

    public HttpGlobalResponseDTO<ProductDTO> getProduct(Long id) {
        HttpGlobalResponseDTO<ProductDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Products> productFound = productsRepository.findById(id);

        if (productFound.isEmpty()) {
            throw new NoSuchElementException("Product not found");
        }

        Products p = productFound.get();
        ProductDTO product = new ProductDTO();
        product.setId(p.getId());
        product.setName(p.getName());
        product.setDescription(p.getDescription());
        product.setPrice(p.getPrice());
        product.setStock(p.getStock());
        product.setCategoryId(p.getCategory().getId());
        product.setCategoryName(p.getCategory().getName());
        product.setSellerId(p.getSeller().getId());
        product.setSellerUsername(p.getSeller().getUsername());

        response.setMessage("Product found");
        response.setData(product);

        return response;
    }

    public HttpGlobalResponseDTO<List<ProductDTO>> getMyProducts(Long sellerId) {
        HttpGlobalResponseDTO<List<ProductDTO>> response = new HttpGlobalResponseDTO<>();
        List<ProductDTO> listProducts = new ArrayList<>();

        List<Products> productsFound = productsRepository.findAll();

        for (Products p : productsFound) {
            if (p.getSeller().getId().equals(sellerId)) {
                ProductDTO product = new ProductDTO();
                product.setId(p.getId());
                product.setName(p.getName());
                product.setDescription(p.getDescription());
                product.setPrice(p.getPrice());
                product.setStock(p.getStock());
                product.setCategoryId(p.getCategory().getId());
                product.setCategoryName(p.getCategory().getName());
                product.setSellerId(p.getSeller().getId());
                product.setSellerUsername(p.getSeller().getUsername());

                listProducts.add(product);
            }
        }

        response.setMessage("Products found");
        response.setData(listProducts);

        return response;
    }

    public HttpGlobalResponseDTO<ProductDTO> createProduct(ProductRequestDTO request, Long sellerId) {
        HttpGlobalResponseDTO<ProductDTO> response = new HttpGlobalResponseDTO<>();
        ProductDTO productDTO = new ProductDTO();

        Optional<Categories> category = categoriesRepository.findById(request.getCategoryId());
        Optional<Users> seller = usersRepository.findById(sellerId);

        if (category.isEmpty()) {
            throw new NoSuchElementException("Category not found");
        }

        if (seller.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        Products product = new Products();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category.get());
        product.setSeller(seller.get());

        productsRepository.save(product);

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setCategoryId(category.get().getId());
        productDTO.setCategoryName(category.get().getName());
        productDTO.setSellerId(seller.get().getId());
        productDTO.setSellerUsername(seller.get().getUsername());

        response.setMessage("Product created successfully");
        response.setData(productDTO);

        return response;
    }

    public HttpGlobalResponseDTO<ProductDTO> updateProduct(Long id, ProductRequestDTO request, Long userId,
            Long rolId) {
        HttpGlobalResponseDTO<ProductDTO> response = new HttpGlobalResponseDTO<>();
        ProductDTO productDTO = new ProductDTO();

        Optional<Products> productFound = productsRepository.findById(id);

        if (productFound.isEmpty()) {
            throw new NoSuchElementException("Product not found");
        }

        Products product = productFound.get();

        if (rolId.equals(RoleEnum.VENDEDOR.getId()) && !product.getSeller().getId().equals(userId)) {
            throw new SecurityException("Product does not belong to the user");
        }

        Optional<Categories> category = categoriesRepository.findById(request.getCategoryId());

        if (category.isEmpty()) {
            throw new NoSuchElementException("Category not found");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category.get());

        productsRepository.save(product);

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setCategoryId(category.get().getId());
        productDTO.setCategoryName(category.get().getName());
        productDTO.setSellerId(product.getSeller().getId());
        productDTO.setSellerUsername(product.getSeller().getUsername());

        response.setMessage("Product updated successfully");
        response.setData(productDTO);

        return response;
    }

    public HttpGlobalResponseDTO<String> deleteProduct(Long id, Long userId, Long rolId) {
        HttpGlobalResponseDTO<String> response = new HttpGlobalResponseDTO<>();
        Optional<Products> productFound = productsRepository.findById(id);

        if (productFound.isEmpty()) {
            throw new NoSuchElementException("Product not found");
        }

        if (rolId.equals(RoleEnum.VENDEDOR.getId()) && !productFound.get().getSeller().getId().equals(userId)) {
            throw new SecurityException("Product does not belong to the user");
        }

        productsRepository.deleteById(id);

        response.setMessage("Product deleted successfully");

        return response;
    }
}

