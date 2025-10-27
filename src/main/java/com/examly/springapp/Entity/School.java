package com.examly.springapp.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String district;
    private String address;
    private String contactInfo;
}