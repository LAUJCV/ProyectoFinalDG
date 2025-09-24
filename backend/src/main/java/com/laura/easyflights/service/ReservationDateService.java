package com.laura.easyflights.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.laura.easyflights.model.Product;
import com.laura.easyflights.model.ReservationDate;
import com.laura.easyflights.model.User;
import com.laura.easyflights.repository.ProductRepository;
import com.laura.easyflights.repository.ReservationDateRepository;
import com.laura.easyflights.repository.UserRepository;

@Service
public class ReservationDateService {

    private final ReservationDateRepository reservationDateRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReservationDateService(ReservationDateRepository reservationDateRepository,
            ProductRepository productRepository, UserRepository userRepository) {
        this.reservationDateRepository = reservationDateRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Método para encontrar un producto por id
    public List<ReservationDate> findProductById(Long productId) {
        return reservationDateRepository.findByProductId(productId);
    }

    // Método para guardar fechas de reservación
    public ReservationDate addReservationDate(Long productId, Long userId, LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productId));

        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Usuario no encontrado"));

        boolean exists = reservationDateRepository.existsByProductAndUser(product, user);

        if (exists) {
            throw new RuntimeException("El usuario ya tiene una reserva para este producto");
        }

        ReservationDate reservationDate = new ReservationDate();
        reservationDate.setLocalDate(date);
        reservationDate.setProduct(product);
        reservationDate.setUser(user);

        return reservationDateRepository.save(reservationDate);
    }

    // Método para guardar fechas
    public ReservationDate saveDate(ReservationDate reservationDate) {
        return reservationDateRepository.save(reservationDate);
    }

    // Método para eliminar por id
    public void deleteById(Long id) {
        reservationDateRepository.deleteById(id);
    }

    // Método para mostrar todas las fechas de reservación disponibles
    public List<ReservationDate> findAllDates() {
        return reservationDateRepository.findAll();
    }

    public boolean userHasReservation(Long userId, Long productId) {
        return reservationDateRepository.existsByUserIdAndProductId(userId, productId);
    }

    public List<LocalDate> getReservedDatesForUserAndProduct(Long userId, Long productId) {
        List<ReservationDate> reservations = reservationDateRepository.findByUserIdAndProductId(userId, productId);
        return reservations.stream()
                .map(ReservationDate::getDate)
                .collect(Collectors.toList());
    }

    public Optional<ReservationDate> findReservationByUserAndProduct(Long userId, Long productId) {
        return reservationDateRepository.findOptionalByUserIdAndProductId(userId, productId);
    }

}
