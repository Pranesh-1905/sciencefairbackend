package com.examly.springapp.controller;

import com.examly.springapp.Entity.Project;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.ProjectService;
import com.examly.springapp.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserRepository userRepository;

    // Student: Create project
    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<?> createProject(@RequestBody Project project, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();
        project.setStudentId(user.getId());
        return ResponseEntity.ok(projectService.createProject(project));
    }

    // Student: Get own projects
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public List<Project> getMyProjects(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) return List.of();
        return projectService.getProjectsByStudentId(user.getId());
    }

    // Admin/Teacher/Judge/Coordinator: Get all projects
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','JUDGE','FAIR_COORDINATOR','COORDINATOR')")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    // Get project by id (role-based)
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        return projectService.getProjectById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Student: Update own project (only if DRAFT)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody Project update, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();
        Project project = projectService.getProjectById(id).orElse(null);
        if (project == null) return ResponseEntity.notFound().build();
        if (!user.getId().equals(project.getStudentId()) && !user.getRole().name().equals("ADMIN"))
            return ResponseEntity.status(403).build();
        if (project.getStatus() != Project.Status.DRAFT && !user.getRole().name().equals("ADMIN"))
            return ResponseEntity.status(403).body("Can only update DRAFT projects");
        update.setStudentId(project.getStudentId());
        return ResponseEntity.ok(projectService.updateProject(id, update));
    }

    // Student: Delete own project (only if DRAFT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<?> deleteProject(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();
        Project project = projectService.getProjectById(id).orElse(null);
        if (project == null) return ResponseEntity.notFound().build();
        if (!user.getId().equals(project.getStudentId()) && !user.getRole().name().equals("ADMIN"))
            return ResponseEntity.status(403).build();
        if (project.getStatus() != Project.Status.DRAFT && !user.getRole().name().equals("ADMIN"))
            return ResponseEntity.status(403).body("Can only delete DRAFT projects");
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}
