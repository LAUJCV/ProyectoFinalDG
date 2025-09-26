package com.laura.easyflights.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.*;

import com.laura.easyflights.dto.AvailabilityRequest;
import com.laura.easyflights.dto.AvailabilityResponse;
import com.laura.easyflights.service.AvailabilityService;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

  private final AvailabilityService service;

  public AvailabilityController(AvailabilityService service) {
    this.service = service;
  }

  // Para el calendario y validación de rango
  @GetMapping("/product/{productId}")
  public AvailabilityResponse check(
      @PathVariable Long productId,
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate) {
    return service.check(productId, startDate, endDate);
  }

  // Pre-reserva opcional: SI NO USAS SECURITY, no intentes leer usuario aquí
  @PostMapping("/pre-reserve")
  public AvailabilityResponse preReserve(@RequestBody AvailabilityRequest req) {
    // Aquí solo validamos el rango; el front ya garantizó que hay sesión
    return service.check(req.productId(), req.startDate(), req.endDate());
  }
}


