package com.examly.springapp.controller;

import com.examly.springapp.Entity.Category;
import com.examly.springapp.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // Admin/Coordinator: Create category
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FAIR_COORDINATOR')")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    // All roles: Get all categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // All roles: Get category by id
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Admin/Coordinator: Update category
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FAIR_COORDINATOR')")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category update) {
        Category updated = categoryService.updateCategory(id, update);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // Admin/Coordinator: Delete category
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FAIR_COORDINATOR')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
