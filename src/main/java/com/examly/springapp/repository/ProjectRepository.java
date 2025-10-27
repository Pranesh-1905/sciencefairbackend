package com.examly.springapp.repository;

import com.examly.springapp.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStudentId(Long studentId);

    // Pagination and filtering
    Page<Project> findByStatus(Project.Status status, Pageable pageable);
    Page<Project> findByStudentId(Long studentId, Pageable pageable);
    Page<Project> findBySchool_Id(Long schoolId, Pageable pageable);
    Page<Project> findByStatusAndSchool_Id(Project.Status status, Long schoolId, Pageable pageable);
    Page<Project> findByStatusAndStudentId(Project.Status status, Long studentId, Pageable pageable);
}
