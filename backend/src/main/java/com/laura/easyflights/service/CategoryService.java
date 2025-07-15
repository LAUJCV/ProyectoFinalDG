package com.laura.easyflights.service;

import java.util.List;
import java.util.Optional;

import com.laura.easyflights.model.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laura.easyflights.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Método para obtener todas las categorías
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Category createCategory(Category category) {
        // Ya llega normalizado, así que no vuelvas a verificar
        return categoryRepository.save(category);
    }

    // Método para eliminar categorías
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public boolean categoryTitleExists(String title) {
        return categoryRepository.existsByTitleIgnoreCase(title);
    }

    public Category saveCategoryWithImage(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public void deleteAllCategories(){
        categoryRepository.deleteAll();
    }

}
