
package com.examly.springapp.Entity;

import jakarta.persistence.*;
import com.examly.springapp.Entity.School;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "users")
public class User {
    public enum Role {
        STUDENT, TEACHER, JUDGE, FAIR_COORDINATOR, ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    private String studentId;
    private String gradeLevel;
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime lastLogin;
    private Boolean isActive = true;
}