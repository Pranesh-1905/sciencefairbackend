package com.examly.springapp.repository;

import com.examly.springapp.Entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByProjectId(Long projectId);
}
