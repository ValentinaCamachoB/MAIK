package com.example.football.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.football.dto.HttpGlobalResponse;
import com.example.football.dto.MessageResponseDto;
import com.example.football.dto.PlayerTrainingDto;
import com.example.football.dto.StartingPlayerDto;
import com.example.football.dto.TrainingRequestDto;
import com.example.football.entity.Training;
import com.example.football.repository.TrainingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    public MessageResponseDto saveTraining(TrainingRequestDto request) {
        for (PlayerTrainingDto playerDto : request.getPlayers()) {
            Training training = new Training();
            
            Double score = (playerDto.getShotPower() * 0.2) 
                        + (playerDto.getSpeed() * 0.3) 
                        + (playerDto.getEffectivePasses() * 0.5);
            
            training.setPlayerName(playerDto.getPlayerName());
            training.setTrainingNumber(request.getTrainingNumber());
            training.setShotPower(playerDto.getShotPower());
            training.setSpeed(playerDto.getSpeed());
            training.setEffectivePasses(playerDto.getEffectivePasses());
            training.setScore(score);
            
            trainingRepository.save(training);
        }

        return new MessageResponseDto("Entrenamiento " + request.getTrainingNumber() + " guardado correctamente");
    }

    public HttpGlobalResponse<List<StartingPlayerDto>> getStartingTeam() {
        HttpGlobalResponse<List<StartingPlayerDto>> response = new HttpGlobalResponse<>();

        Optional<List<Training>> entreno1 = Optional.ofNullable(trainingRepository.findByTrainingNumber(1));
        Optional<List<Training>> entreno2 = Optional.ofNullable(trainingRepository.findByTrainingNumber(2));
        Optional<List<Training>> entreno3 = Optional.ofNullable(trainingRepository.findByTrainingNumber(3));

        if (entreno1.isEmpty() || entreno1.get().isEmpty() ||
            entreno2.isEmpty() || entreno2.get().isEmpty() ||
            entreno3.isEmpty() || entreno3.get().isEmpty()) {
            response.setMessage("Faltan entrenamientos por registrar");
            return response;
        }

        List<Training> allTrainings = trainingRepository.findAll();

        List<String> uniqueNames = new ArrayList<>();
        for (Training t : allTrainings) {
            if (!uniqueNames.contains(t.getPlayerName())) {
                uniqueNames.add(t.getPlayerName());
            }
        }

        List<StartingPlayerDto> averages = new ArrayList<>();
        for (String name : uniqueNames) {
            Double total = 0.0;
            Integer count = 0;

            for (Training t : allTrainings) {
                if (t.getPlayerName().equals(name)) {
                    total = total + t.getScore();
                    count = count + 1;
                }
            }

            Double average = total / count;

            StartingPlayerDto player = new StartingPlayerDto();
            player.setPlayerName(name);
            player.setScore(average);
            averages.add(player);
        }

        for (int i = 0; i < averages.size(); i++) {
            for (int j = 0; j < averages.size() - 1; j++) {
                StartingPlayerDto actual = averages.get(j);
                StartingPlayerDto siguiente = averages.get(j + 1);

                if (actual.getScore() < siguiente.getScore()) {
                    averages.set(j, siguiente);
                    averages.set(j + 1, actual);
                }
            }
        }

        List<StartingPlayerDto> startingTeam = new ArrayList<>();
        for (int i = 0; i < 5 && i < averages.size(); i++) {
            startingTeam.add(averages.get(i));
        }

        response.setMessage("Equipo titular obtenido correctamente");
        response.setData(startingTeam);
        return response;
    }
}
