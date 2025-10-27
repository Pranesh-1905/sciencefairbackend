package com.examly.springapp.repository;

import com.examly.springapp.Entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {
    boolean existsByName(String name);
    Optional<School> findByName(String name);

    // Pagination and filtering
    Page<School> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<School> findByDistrictContainingIgnoreCase(String district, Pageable pageable);
    Page<School> findByNameContainingIgnoreCaseAndDistrictContainingIgnoreCase(String name, String district, Pageable pageable);
}
