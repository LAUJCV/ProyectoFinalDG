package com.laura.easyflights.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

import com.laura.easyflights.dto.ProductDTO;
import com.laura.easyflights.model.Product;
import com.laura.easyflights.model.Review;
import com.laura.easyflights.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    // Metodo para obtener todos los productos
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    // Metodo para obtener producto por id
    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    // Método para agregar productos
    public Product addProduct(Product product) {
        if (repository.existsByName(product.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El producto ya existe");
        }

        return repository.save(product);
    }

    // Método para eliminar un producto
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    // Método para mostrar la calificación del producto
    public List<ProductDTO> getAllProductsWithAverageRating() {
        List<Product> products = repository.findAll();

        return products.stream().map(product -> {
            List<Review> reviews = product.getReviews();
            double averageRating = reviews.isEmpty()
            ? 0.0
            : reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

            return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImages(),
                averageRating
            );
        }).collect(Collectors.toList());
     
    }
}
