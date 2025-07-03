package com.laura.easyflights.repository;

import com.laura.easyflights.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{

    boolean existsByName(String name);
}
