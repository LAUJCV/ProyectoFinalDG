package com.laura.easyflights.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.laura.easyflights.dto.ProductDTO;
import com.laura.easyflights.model.Product;
import com.laura.easyflights.model.Review;
import com.laura.easyflights.repository.ProductRepository;
import com.laura.easyflights.repository.ReservationRepository;
import com.laura.easyflights.repository.ReservationDateRepository;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final ReservationRepository reservationRepository;
    private final ReservationDateRepository reservationDateRepository;

    public ProductService(ProductRepository repository,
                          ReservationRepository reservationRepository,
                          ReservationDateRepository reservationDateRepository) {
        this.repository = repository;
        this.reservationRepository = reservationRepository;
        this.reservationDateRepository = reservationDateRepository;
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

    // productos disponibles por rango de fechas
    public List<Product> findAvailableProducts(LocalDate start, LocalDate end) {
        if (start == null || end == null || end.isBefore(start)) {
            throw new IllegalArgumentException("Rango de fechas inválido");
        }

        List<Product> all = repository.findAll();
        List<Product> result = new ArrayList<>();

        for (Product p : all) {
            // Revisar solapamiento con reservas
            boolean overlap = !reservationRepository
                .findByProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    p.getId(), end, start
                ).isEmpty();
            if (overlap) continue;

            // Revisar si hay días bloqueados en ese rango
            boolean blocked = reservationDateRepository
                .existsByProductIdAndDateBetween(p.getId(), start, end);
            if (blocked) continue;

            result.add(p);
        }
        return result;
    }
}
