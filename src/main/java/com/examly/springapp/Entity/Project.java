package com.examly.springapp.Entity;

import jakarta.persistence.*;
import com.examly.springapp.Entity.School;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "projects")
public class Project {
    public enum Status {
        DRAFT, SUBMITTED, UNDER_REVIEW, EVALUATED, AWARDED, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String category;

    @Column(nullable=false)
    private String gradeLevel;

    @Column(length=1000)
    private String abstractText;

    @Column(length=2000)
    private String methodology;

    @Column(nullable=false)
    private Long studentId; // FK to User

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private LocalDateTime submissionDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;

    private Double totalScore;
}