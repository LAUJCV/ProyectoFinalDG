package com.laura.easyflights.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laura.easyflights.model.Product;
import com.laura.easyflights.model.Review;
import com.laura.easyflights.model.User;
import com.laura.easyflights.repository.ProductRepository;
import com.laura.easyflights.repository.ReviewRepository;
import com.laura.easyflights.repository.UserRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Método para guardar una review
    public Review saveReview(Long productId, Long userId, int rating, String comment) {
        Product product = productRepository.findById(productId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Review review = new Review(rating, comment, product, user);

        return reviewRepository.save(review);

    }

    // Consultar reviews por producto
    public List<Review> findReviewsByProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();

        return reviewRepository.findByProduct(product);
    }

}
