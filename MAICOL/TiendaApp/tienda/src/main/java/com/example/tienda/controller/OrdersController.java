package com.example.tienda.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tienda.dto.HttpGlobalResponseDTO;
import com.example.tienda.dto.OrderDTO;
import com.example.tienda.dto.OrderRequestDTO;
import com.example.tienda.dto.OrderStatusRequestDTO;
import com.example.tienda.enums.RoleEnum;
import com.example.tienda.security.RequiresRole;
import com.example.tienda.service.OrdersService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    /**
     * Servicio de pedidos
     */
    private final OrdersService ordersService;

    @PostMapping
    @RequiresRole({ RoleEnum.CLIENTE })
    public ResponseEntity<HttpGlobalResponseDTO<OrderDTO>> createOrder(@RequestBody OrderRequestDTO body, HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderDTO> response = new HttpGlobalResponseDTO<>();

        try {
            Long userId = (Long) request.getAttribute("userId");
            response = ordersService.createOrder(body, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NoSuchElementException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/mine")
    @RequiresRole({ RoleEnum.CLIENTE })
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderDTO>>> getMyOrders(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<OrderDTO>> response = new HttpGlobalResponseDTO<>();

        try {
            Long userId = (Long) request.getAttribute("userId");
            response = ordersService.getMyOrders(userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/sales")
    @RequiresRole({ RoleEnum.VENDEDOR })
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderDTO>>> getSales(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<OrderDTO>> response = new HttpGlobalResponseDTO<>();

        try {
            Long userId = (Long) request.getAttribute("userId");
            response = ordersService.getSales(userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    @RequiresRole({ RoleEnum.ADMIN })
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderDTO>>> getAllOrders() {
        HttpGlobalResponseDTO<List<OrderDTO>> response = new HttpGlobalResponseDTO<>();

        try {
            response = ordersService.getAllOrders();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PatchMapping("/{id}/status")
    @RequiresRole({ RoleEnum.VENDEDOR, RoleEnum.ADMIN })
    public ResponseEntity<HttpGlobalResponseDTO<OrderDTO>> changeStatus(@PathVariable Long id, @RequestBody OrderStatusRequestDTO body, HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderDTO> response = new HttpGlobalResponseDTO<>();

        try {
            Long userId = (Long) request.getAttribute("userId");
            Long rolId = (Long) request.getAttribute("rolId");
            response = ordersService.changeStatus(id, body, userId, rolId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchElementException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (SecurityException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (IllegalArgumentException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (IllegalStateException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PatchMapping("/{id}/cancel")
    @RequiresRole({ RoleEnum.CLIENTE })
    public ResponseEntity<HttpGlobalResponseDTO<OrderDTO>> cancelOrder(@PathVariable Long id, HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderDTO> response = new HttpGlobalResponseDTO<>();

        try {
            Long userId = (Long) request.getAttribute("userId");
            response = ordersService.cancelOrder(id, userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchElementException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (SecurityException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (IllegalStateException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

