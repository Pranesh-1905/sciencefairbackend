package com.examly.springapp.controller;

import com.examly.springapp.Entity.School;
import com.examly.springapp.service.SchoolService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/schools")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    // ADMIN: Create school
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSchool(@RequestBody School school) {
        return ResponseEntity.ok(schoolService.createSchool(school));
    }

    // All roles: Get all schools
    @GetMapping
    public List<School> getAllSchools() {
        return schoolService.getAllSchools();
    }

    // All roles: Get school by id
    @GetMapping("/{id}")
    public ResponseEntity<School> getSchool(@PathVariable Long id) {
        return schoolService.getSchoolById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // ADMIN: Update school
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSchool(@PathVariable Long id, @RequestBody School update) {
        School updated = schoolService.updateSchool(id, update);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // ADMIN: Delete school
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.ok().build();
    }
}
