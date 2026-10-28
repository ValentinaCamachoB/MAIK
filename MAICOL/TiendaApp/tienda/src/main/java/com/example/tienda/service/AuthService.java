package com.example.tienda.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tienda.dto.HttpGlobalResponseDTO;
import com.example.tienda.dto.LoginRequestDTO;
import com.example.tienda.dto.LoginResponseDTO;
import com.example.tienda.dto.RegisterRequestDTO;
import com.example.tienda.dto.UserDTO;
import com.example.tienda.entity.Roles;
import com.example.tienda.entity.Users;
import com.example.tienda.enums.RoleEnum;
import com.example.tienda.repository.RolesRepository;
import com.example.tienda.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

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

    /**
     * Servicio de jwt
     */
    private final JwtService jwtService;

    public HttpGlobalResponseDTO<UserDTO> register(RegisterRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Optional<Users> usernameFound = usersRepository.findByUsername(request.getUsername());
        Optional<Users> emailFound = usersRepository.findByEmail(request.getEmail());
        Optional<Roles> rol = rolesRepository.findById(RoleEnum.CLIENTE.getId());

        if (usernameFound.isPresent()) {
            response.setMessage("Username is already in use");
            return response;
        }

        if (emailFound.isPresent()) {
            response.setMessage("Email is already in use");
            return response;
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

        response.setMessage("User registered successfully");
        response.setData(userDTO);

        return response;
    }

    public HttpGlobalResponseDTO<LoginResponseDTO> login(LoginRequestDTO request) {
        HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Users> email = usersRepository.findByEmail(request.getUser());
        Optional<Users> username = usersRepository.findByUsername(request.getUser());

        if (username.isEmpty() && email.isEmpty()) {
            response.setMessage("Incorrect user or password");
            return response;
        }

        Users userFound;

        if (email.isPresent()) {
            userFound = email.get();
        } else {
            userFound = username.get();
        }

        if (!passwordEncoder.matches(request.getPassword(), userFound.getPassword())) {
            response.setMessage("Incorrect user or password");
            return response;
        }

        if (!userFound.getActive()) {
            response.setMessage("User is inactive");
            return response;
        }

        String jwt = jwtService.generateToken(userFound.getId(), userFound.getUsername(), userFound.getRoles().getId());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userFound.getId());
        userDTO.setUsername(userFound.getUsername());
        userDTO.setEmail(userFound.getEmail());
        userDTO.setActive(userFound.getActive());
        userDTO.setRol(userFound.getRoles().getName());

        LoginResponseDTO token = new LoginResponseDTO();
        token.setJwt(jwt);
        token.setUser(userDTO);
        response.setMessage("Login successful");
        response.setData(token);

        return response;
    }

    public HttpGlobalResponseDTO<LoginResponseDTO> refreshToken(String token) throws Exception {
        HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();
        LoginResponseDTO refresh = new LoginResponseDTO();
        String jwt = jwtService.refreshToken(token);
        Long userId = jwtService.extractUserId(token);
        Optional<Users> userFound = usersRepository.findById(userId);
        Users user = userFound.get();

        if (!user.getActive()) {
            throw new Exception("User is inactive");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.getActive());
        userDTO.setRol(user.getRoles().getName());
        refresh.setJwt(jwt);
        refresh.setUser(userDTO);
        response.setMessage("Token refreshed successfully");
        response.setData(refresh);
        return response;
    }
}
