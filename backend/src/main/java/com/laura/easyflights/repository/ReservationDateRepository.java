package com.laura.easyflights.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laura.easyflights.model.Product;
import com.laura.easyflights.model.ReservationDate;
import com.laura.easyflights.model.User;

public interface ReservationDateRepository extends JpaRepository<ReservationDate, Long> {
    List<ReservationDate> findByProductId(Long productId);
    boolean existsByProductIdAndDate(Long productId, LocalDate date);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    boolean existsByProductAndUser(Product product, User user);
    List<ReservationDate> findByUserIdAndProductId(Long userId, Long productId);
    Optional<ReservationDate> findOptionalByUserIdAndProductId(Long userId, Long productId);
     // ¿Hay días ocupados entre start y end (INCLUSIVO)?
     boolean existsByProductIdAndDateBetween(Long productId, LocalDate start, LocalDate end);

     // Traer los días ocupados para pintarlos/bloquearlos en el calendario
     List<ReservationDate> findByProductIdAndDateBetween(Long productId, LocalDate start, LocalDate end);
    
}
