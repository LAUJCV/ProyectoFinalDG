package com.laura.easyflights.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laura.easyflights.model.ReservationDate;
import com.laura.easyflights.service.ReservationDateService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationDateController {

    private final ReservationDateService reservationDateService;

    public ReservationDateController(ReservationDateService reservationDateService) {
        this.reservationDateService = reservationDateService;
    }

    // endpoint para obtener fechas ocupadas de un producto
    @GetMapping("/{id}/reserved-dates")
    public List<String> getReserverDates(@PathVariable Long id) {
        List<ReservationDate> dates = reservationDateService.findProductById(id);
        return dates.stream()
                .map(d -> d.getDate().toString())
                .collect(Collectors.toList());
    }

    // Endpoint para agregar fechas de reservación
    @PostMapping("/{id}/reserved-dates")
    public ResponseEntity<String> addReservationDate(@PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        try {
            String dateString = payload.get("date");
            Long userId = Long.parseLong(payload.get("userId"));
            LocalDate date = LocalDate.parse(dateString);

            reservationDateService.addReservationDate(id, userId, date);

            return ResponseEntity.ok("Fecha agregada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la fecha: " + e.getMessage());
        }
    }

    @DeleteMapping("/reserved-dates/{id}")
    public void deleteById(@PathVariable Long id) {
        reservationDateService.deleteById(id);
    }

    @GetMapping("/has-reserved")
    public ResponseEntity<Boolean> hasUserReservedProduct(@RequestParam Long userId, @RequestParam Long productId) {

        boolean hasReserved = reservationDateService.userHasReservation(userId, productId);

        return ResponseEntity.ok(hasReserved);
    }

    @GetMapping("/reservation-date")
    public ResponseEntity<String> getUserReservationDate(@RequestParam Long userId, @RequestParam Long productId) {
        return reservationDateService.findReservationByUserAndProduct(userId, productId)
                .map(r -> ResponseEntity.ok(r.getDate().toString()))
                .orElse(ResponseEntity.notFound().build());
    }
}
