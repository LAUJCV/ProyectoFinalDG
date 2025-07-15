package com.laura.easyflights.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laura.easyflights.model.Feature;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
    boolean existsByNameIgnoreCase(String name);
}
