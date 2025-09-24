package com.laura.easyflights.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laura.easyflights.model.Product;
import com.laura.easyflights.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByProduct(Product product);
}
