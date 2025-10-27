package com.examly.springapp.service;

import com.examly.springapp.Entity.Project;
import com.examly.springapp.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> getProjectsByStudentId(Long studentId) {
        return projectRepository.findByStudentId(studentId);
    }

    public Project updateProject(Long id, Project update) {
        return projectRepository.findById(id).map(project -> {
            project.setTitle(update.getTitle());
            project.setCategory(update.getCategory());
            project.setGradeLevel(update.getGradeLevel());
            project.setAbstractText(update.getAbstractText());
            project.setMethodology(update.getMethodology());
            project.setStatus(update.getStatus());
            project.setTotalScore(update.getTotalScore());
            return projectRepository.save(project);
        }).orElse(null);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
