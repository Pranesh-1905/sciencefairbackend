package com.examly.springapp.controller;

import com.examly.springapp.Entity.Submission;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.SubmissionService;
import com.examly.springapp.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private UserRepository userRepository;

    // STUDENT/ADMIN: Create submission
    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<?> createSubmission(@RequestBody Submission submission, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();
        // Optionally, check if user owns the project (not shown here)
        return ResponseEntity.ok(submissionService.createSubmission(submission));
    }

    // STUDENT/ADMIN/TEACHER/COORDINATOR: Get submissions for a project
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN','TEACHER','FAIR_COORDINATOR','COORDINATOR')")
    public List<Submission> getSubmissionsByProject(@PathVariable Long projectId) {
        return submissionService.getSubmissionsByProjectId(projectId);
    }

    // ADMIN/TEACHER/COORDINATOR: Get all submissions
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','FAIR_COORDINATOR','COORDINATOR')")
    public List<Submission> getAllSubmissions() {
        return submissionService.getAllSubmissions();
    }

    // STUDENT/ADMIN: Update submission
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<?> updateSubmission(@PathVariable Long id, @RequestBody Submission update, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();
        // Optionally, check if user owns the submission/project (not shown here)
        Submission updated = submissionService.updateSubmission(id, update);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // STUDENT/ADMIN: Delete submission
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<?> deleteSubmission(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();
        // Optionally, check if user owns the submission/project (not shown here)
        submissionService.deleteSubmission(id);
        return ResponseEntity.ok().build();
    }
}
