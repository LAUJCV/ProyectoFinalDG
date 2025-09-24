package com.laura.easyflights.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laura.easyflights.model.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Long>{
    boolean existsByTitle(String title);
}
