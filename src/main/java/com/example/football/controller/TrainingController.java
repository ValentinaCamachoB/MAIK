package com.example.football.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.football.dto.HttpGlobalResponse;
import com.example.football.dto.MessageResponseDto;
import com.example.football.dto.StartingPlayerDto;
import com.example.football.dto.TrainingRequestDto;
import com.example.football.service.TrainingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/trainings") 
@RequiredArgsConstructor 
public class TrainingController {

    // Inyeccion de dependencias
    private final TrainingService trainingService;

    @PostMapping("/save")
    public ResponseEntity<MessageResponseDto> saveTraining(@Valid @RequestBody TrainingRequestDto request) {
        try {
            MessageResponseDto response = trainingService.saveTraining(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/starting-team")
    public HttpGlobalResponse<List<StartingPlayerDto>> getStartingTeam() {
        return trainingService.getStartingTeam();
    }
}
