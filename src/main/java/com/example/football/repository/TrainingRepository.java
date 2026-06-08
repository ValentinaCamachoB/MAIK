package com.example.football.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.football.entity.Training;

@Repository 
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByTrainingNumber(Integer trainingNumber);
}
