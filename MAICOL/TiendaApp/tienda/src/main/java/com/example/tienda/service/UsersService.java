package com.example.tienda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tienda.dto.ChangeRoleRequestDTO;
import com.example.tienda.dto.ChangeStatusRequestDTO;
import com.example.tienda.dto.CreateUserRequestDTO;
import com.example.tienda.dto.HttpGlobalResponseDTO;
import com.example.tienda.dto.UserDTO;
import com.example.tienda.entity.Roles;
import com.example.tienda.entity.Users;
import com.example.tienda.repository.RolesRepository;
import com.example.tienda.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    /**
     * Repositorio de usuarios
     */
    private final UsersRepository usersRepository;

    /**
     * Repositorio de roles
     */
    private final RolesRepository rolesRepository;

    /**
     * Encriptador de contraseñas
     */
    private final PasswordEncoder passwordEncoder;

    public HttpGlobalResponseDTO<UserDTO> getMe(Long userId) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Users> userFound = usersRepository.findById(userId);

        if (userFound.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        Users u = userFound.get();
        UserDTO user = new UserDTO();
        user.setId(u.getId());
        user.setUsername(u.getUsername());
        user.setEmail(u.getEmail());
        user.setActive(u.getActive());
        user.setRol(u.getRoles().getName());

        response.setMessage("User found");
        response.setData(user);

        return response;
    }

    public HttpGlobalResponseDTO<List<UserDTO>> getUsers() {
        HttpGlobalResponseDTO<List<UserDTO>> response = new HttpGlobalResponseDTO<>();
        List<UserDTO> listUsers = new ArrayList<>();

        List<Users> usersFound = usersRepository.findAll();

        for (Users u : usersFound) {
            UserDTO user = new UserDTO();
            user.setId(u.getId());
            user.setUsername(u.getUsername());
            user.setEmail(u.getEmail());
            user.setActive(u.getActive());
            user.setRol(u.getRoles().getName());

            listUsers.add(user);
        }

        response.setMessage("Users found");
        response.setData(listUsers);

        return response;
    }

    public HttpGlobalResponseDTO<UserDTO> createUser(CreateUserRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Optional<Users> usernameFound = usersRepository.findByUsername(request.getUsername());
        Optional<Users> emailFound = usersRepository.findByEmail(request.getEmail());
        Optional<Roles> rol = rolesRepository.findById(request.getRolId());

        if (usernameFound.isPresent()) {
            throw new IllegalStateException("Username is already in use");
        }

        if (emailFound.isPresent()) {
            throw new IllegalStateException("Email is already in use");
        }

        if (rol.isEmpty()) {
            throw new NoSuchElementException("Role not found");
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setRoles(rol.get());

        usersRepository.save(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.getActive());
        userDTO.setRol(rol.get().getName());

        response.setMessage("User created successfully");
        response.setData(userDTO);

        return response;
    }

    public HttpGlobalResponseDTO<UserDTO> changeRole(Long id, ChangeRoleRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Optional<Users> userFound = usersRepository.findById(id);
        Optional<Roles> rol = rolesRepository.findById(request.getRolId());

        if (userFound.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        if (rol.isEmpty()) {
            throw new NoSuchElementException("Role not found");
        }

        Users user = userFound.get();
        user.setRoles(rol.get());

        usersRepository.save(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.getActive());
        userDTO.setRol(rol.get().getName());

        response.setMessage("Role updated successfully");
        response.setData(userDTO);

        return response;
    }

    public HttpGlobalResponseDTO<UserDTO> changeStatus(Long id, ChangeStatusRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Optional<Users> userFound = usersRepository.findById(id);

        if (userFound.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        Users user = userFound.get();
        user.setActive(request.getActive());

        usersRepository.save(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.getActive());
        userDTO.setRol(user.getRoles().getName());

        response.setMessage("User status updated successfully");
        response.setData(userDTO);

        return response;
    }
}

