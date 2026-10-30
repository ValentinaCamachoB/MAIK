package com.example.tienda.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.tienda.dto.HttpGlobalResponseDTO;
import com.example.tienda.dto.OrderDTO;
import com.example.tienda.dto.OrderRequestDTO;
import com.example.tienda.dto.OrderStatusRequestDTO;
import com.example.tienda.entity.Orders;
import com.example.tienda.entity.Products;
import com.example.tienda.entity.Users;
import com.example.tienda.enums.OrderStatusEnum;
import com.example.tienda.enums.RoleEnum;
import com.example.tienda.repository.OrdersRepository;
import com.example.tienda.repository.ProductsRepository;
import com.example.tienda.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {

    /**
     * Repositorio de pedidos
     */
    private final OrdersRepository ordersRepository;

    /**
     * Repositorio de productos
     */
    private final ProductsRepository productsRepository;

    /**
     * Repositorio de usuarios
     */
    private final UsersRepository usersRepository;

    public HttpGlobalResponseDTO<OrderDTO> createOrder(OrderRequestDTO request, Long clientId) {
        HttpGlobalResponseDTO<OrderDTO> response = new HttpGlobalResponseDTO<>();
        OrderDTO orderDTO = new OrderDTO();

        Optional<Products> productFound = productsRepository.findById(request.getProductId());
        Optional<Users> clientFound = usersRepository.findById(clientId);

        if (productFound.isEmpty()) {
            throw new NoSuchElementException("Product not found");
        }

        if (clientFound.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        Products product = productFound.get();

        if (request.getQuantity() <= 0 || product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock");
        }

        product.setStock(product.getStock() - request.getQuantity());
        productsRepository.save(product);

        Orders order = new Orders();
        order.setClient(clientFound.get());
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setTotal(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        order.setStatus(OrderStatusEnum.PENDIENTE);
        order.setCreatedAt(LocalDateTime.now());

        ordersRepository.save(order);

        orderDTO.setId(order.getId());
        orderDTO.setClientId(order.getClient().getId());
        orderDTO.setProductId(product.getId());
        orderDTO.setProductName(product.getName());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setTotal(order.getTotal());
        orderDTO.setStatus(order.getStatus().name());
        orderDTO.setCreatedAt(order.getCreatedAt());

        response.setMessage("Order created successfully");
        response.setData(orderDTO);

        return response;
    }

    public HttpGlobalResponseDTO<List<OrderDTO>> getMyOrders(Long clientId) {
        HttpGlobalResponseDTO<List<OrderDTO>> response = new HttpGlobalResponseDTO<>();
        List<OrderDTO> listOrders = new ArrayList<>();

        List<Orders> ordersFound = ordersRepository.findAll();

        for (Orders o : ordersFound) {
            if (o.getClient().getId().equals(clientId)) {
                OrderDTO order = new OrderDTO();
                order.setId(o.getId());
                order.setClientId(o.getClient().getId());
                order.setProductId(o.getProduct().getId());
                order.setProductName(o.getProduct().getName());
                order.setQuantity(o.getQuantity());
                order.setTotal(o.getTotal());
                order.setStatus(o.getStatus().name());
                order.setCreatedAt(o.getCreatedAt());

                listOrders.add(order);
            }
        }

        response.setMessage("Orders found");
        response.setData(listOrders);

        return response;
    }

    public HttpGlobalResponseDTO<List<OrderDTO>> getSales(Long sellerId) {
        HttpGlobalResponseDTO<List<OrderDTO>> response = new HttpGlobalResponseDTO<>();
        List<OrderDTO> listOrders = new ArrayList<>();

        List<Orders> ordersFound = ordersRepository.findAll();

        for (Orders o : ordersFound) {
            if (o.getProduct().getSeller().getId().equals(sellerId)) {
                OrderDTO order = new OrderDTO();
                order.setId(o.getId());
                order.setClientId(o.getClient().getId());
                order.setProductId(o.getProduct().getId());
                order.setProductName(o.getProduct().getName());
                order.setQuantity(o.getQuantity());
                order.setTotal(o.getTotal());
                order.setStatus(o.getStatus().name());
                order.setCreatedAt(o.getCreatedAt());

                listOrders.add(order);
            }
        }

        response.setMessage("Orders found");
        response.setData(listOrders);

        return response;
    }

    public HttpGlobalResponseDTO<List<OrderDTO>> getAllOrders() {
        HttpGlobalResponseDTO<List<OrderDTO>> response = new HttpGlobalResponseDTO<>();
        List<OrderDTO> listOrders = new ArrayList<>();

        List<Orders> ordersFound = ordersRepository.findAll();

        for (Orders o : ordersFound) {
            OrderDTO order = new OrderDTO();
            order.setId(o.getId());
            order.setClientId(o.getClient().getId());
            order.setProductId(o.getProduct().getId());
            order.setProductName(o.getProduct().getName());
            order.setQuantity(o.getQuantity());
            order.setTotal(o.getTotal());
            order.setStatus(o.getStatus().name());
            order.setCreatedAt(o.getCreatedAt());

            listOrders.add(order);
        }

        response.setMessage("Orders found");
        response.setData(listOrders);

        return response;
    }

    public HttpGlobalResponseDTO<OrderDTO> changeStatus(Long id, OrderStatusRequestDTO request, Long userId,
            Long rolId) {
        HttpGlobalResponseDTO<OrderDTO> response = new HttpGlobalResponseDTO<>();
        OrderDTO orderDTO = new OrderDTO();

        Optional<Orders> orderFound = ordersRepository.findById(id);

        if (orderFound.isEmpty()) {
            throw new NoSuchElementException("Order not found");
        }

        Orders order = orderFound.get();

        if (rolId.equals(RoleEnum.VENDEDOR.getId()) && !order.getProduct().getSeller().getId().equals(userId)) {
            throw new SecurityException("Order does not belong to a product of the user");
        }

        OrderStatusEnum newStatus;

        try {
            newStatus = OrderStatusEnum.valueOf(request.getStatus());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status");
        }

        if (order.getStatus() == OrderStatusEnum.CANCELADO) {
            throw new IllegalStateException("Order is already cancelled");
        }

        if (newStatus == OrderStatusEnum.CANCELADO) {
            Products product = order.getProduct();
            product.setStock(product.getStock() + order.getQuantity());
            productsRepository.save(product);
        }

        order.setStatus(newStatus);
        ordersRepository.save(order);

        orderDTO.setId(order.getId());
        orderDTO.setClientId(order.getClient().getId());
        orderDTO.setProductId(order.getProduct().getId());
        orderDTO.setProductName(order.getProduct().getName());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setTotal(order.getTotal());
        orderDTO.setStatus(order.getStatus().name());
        orderDTO.setCreatedAt(order.getCreatedAt());

        response.setMessage("Order status updated successfully");
        response.setData(orderDTO);

        return response;
    }

    public HttpGlobalResponseDTO<OrderDTO> cancelOrder(Long id, Long clientId) {
        HttpGlobalResponseDTO<OrderDTO> response = new HttpGlobalResponseDTO<>();
        OrderDTO orderDTO = new OrderDTO();

        Optional<Orders> orderFound = ordersRepository.findById(id);

        if (orderFound.isEmpty()) {
            throw new NoSuchElementException("Order not found");
        }

        Orders order = orderFound.get();

        if (!order.getClient().getId().equals(clientId)) {
            throw new SecurityException("Order does not belong to the user");
        }

        if (order.getStatus() != OrderStatusEnum.PENDIENTE) {
            throw new IllegalStateException("Only PENDIENTE orders can be cancelled");
        }

        Products product = order.getProduct();
        product.setStock(product.getStock() + order.getQuantity());
        productsRepository.save(product);

        order.setStatus(OrderStatusEnum.CANCELADO);
        ordersRepository.save(order);

        orderDTO.setId(order.getId());
        orderDTO.setClientId(order.getClient().getId());
        orderDTO.setProductId(product.getId());
        orderDTO.setProductName(product.getName());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setTotal(order.getTotal());
        orderDTO.setStatus(order.getStatus().name());
        orderDTO.setCreatedAt(order.getCreatedAt());

        response.setMessage("Order cancelled successfully");
        response.setData(orderDTO);

        return response;
    }
}
