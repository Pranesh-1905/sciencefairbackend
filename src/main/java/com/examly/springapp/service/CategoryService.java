package com.examly.springapp.service;

import com.examly.springapp.Entity.Category;
import com.examly.springapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category updateCategory(Long id, Category update) {
        return categoryRepository.findById(id).map(category -> {
            category.setCategoryName(update.getCategoryName());
            category.setDescription(update.getDescription());
            category.setGradeLevel(update.getGradeLevel());
            category.setCriteria(update.getCriteria());
            category.setMaxProjects(update.getMaxProjects());
            return categoryRepository.save(category);
        }).orElse(null);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
