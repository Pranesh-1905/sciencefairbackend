package com.examly.springapp.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long projectId;

    private String fileType;
    private String filePath;
    private LocalDateTime submissionDate = LocalDateTime.now();
    private Long fileSize;
    private String status;
}
