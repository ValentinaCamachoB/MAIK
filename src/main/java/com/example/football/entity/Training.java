package com.example.football.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Table(name = "trainings") 
@Data 
public class Training {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(name = "player_name") 
    private String playerName;

    @Column(name = "training_number") 
    private Integer trainingNumber;

    @Column(name = "shot_power") 
    private Double shotPower;

    @Column(name = "speed") 
    private Double speed;

    @Column(name = "effective_passes") 
    private Integer effectivePasses;

    @Column(name = "score")
    private Double score;
}
