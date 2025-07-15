package com.laura.easyflights.repository;

import com.laura.easyflights.model.Category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface  CategoryRepository extends JpaRepository<Category, Long>{
    Optional<Category> findByTitleIgnoreCase(String title);
    Optional<Category> findById(Long id);
    boolean existsByTitleIgnoreCase(String title);
    
}
