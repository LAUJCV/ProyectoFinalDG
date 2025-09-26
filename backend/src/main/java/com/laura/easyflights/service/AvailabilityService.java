package com.laura.easyflights.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.laura.easyflights.dto.AvailabilityResponse;
import com.laura.easyflights.model.ReservationDate;
import com.laura.easyflights.repository.ReservationDateRepository;
import com.laura.easyflights.repository.ReservationRepository;

// AvailabilityService.java
@Service
public class AvailabilityService {
  private final ReservationRepository reservations;
  private final ReservationDateRepository dates;

  public AvailabilityService(ReservationRepository reservations,
                             ReservationDateRepository dates) {
    this.reservations = reservations;
    this.dates = dates;
  }

  public AvailabilityResponse check(Long productId, LocalDate start, LocalDate end) {
    if (start == null || end == null || end.isBefore(start)) {
      return new AvailabilityResponse(false, List.of(), "Rango de fechas inválido");
    }

    // Choques por rango en reservas activas
    var overlaps = reservations.findByProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(productId, start, end);
    if (!overlaps.isEmpty()) {
      return new AvailabilityResponse(false, List.of(), "Ya existe una reserva en ese rango");
    }

    // Días ya ocupados/bloqueados día a día
    var blockedDays = dates.findByProductIdAndDateBetween(productId, start, end)
                           .stream().map(ReservationDate::getDate).toList();
    if (!blockedDays.isEmpty()) {
      return new AvailabilityResponse(false, blockedDays, "Hay días no disponibles en el rango");
    }

    return new AvailabilityResponse(true, List.of(), "Disponible");
  }
}

