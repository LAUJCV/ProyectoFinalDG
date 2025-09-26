package com.laura.easyflights.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.laura.easyflights.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserIdOrderByStartDateDesc(Long userId);
     // Detectar solapamientos: (product = X) AND (start <= endParam) AND (end >= startParam)
    List<Reservation> findByProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long productId, LocalDate endDate, LocalDate startDate);
}
