package com.examly.springapp.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "evaluations")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("projectId")
    private Long projectId;

    @Column(nullable = false)
    @JsonProperty("judgeId")
    private Long judgeId;

    @Column(nullable = false)
    @JsonProperty("creativity")
    private Double creativity;

    @Column(nullable = false)
    @JsonProperty("methodology")
    private Double methodology;

    @Column(nullable = false)
    @JsonProperty("presentation")
    private Double presentation;

    @Column(name = "total_score")
    private Double totalScore;
    
    @JsonProperty("feedback")
    private String feedback;
    
    private LocalDateTime evaluationDate = LocalDateTime.now();
}
