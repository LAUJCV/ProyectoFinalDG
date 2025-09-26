package com.laura.easyflights.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.laura.easyflights.controller.EmailServer;
import com.laura.easyflights.dto.ReservationCreateRequest;
import com.laura.easyflights.dto.ReservationSummaryResponse;
import com.laura.easyflights.model.Product;
import com.laura.easyflights.model.Reservation;
import com.laura.easyflights.model.ReservationDate;
import com.laura.easyflights.model.ReservationStatus;
import com.laura.easyflights.model.User;
import com.laura.easyflights.repository.ProductRepository;
import com.laura.easyflights.repository.ReservationDateRepository;
import com.laura.easyflights.repository.ReservationRepository;
import com.laura.easyflights.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservations;
    private final ReservationDateRepository dates;
    private final ProductRepository products;
    private final UserRepository users;
    private final EmailServer emailService;

    public ReservationService(ReservationRepository reservations,
            ReservationDateRepository dates,
            ProductRepository products,
            UserRepository users, EmailServer emailService) {
        this.reservations = reservations;
        this.dates = dates;
        this.products = products;
        this.users = users;
        this.emailService = emailService;
    }

    @Transactional
    public ReservationSummaryResponse create(ReservationCreateRequest req) {
        // Validaciones básicas
        if (req.productId() == null || req.userId() == null ||
                req.startDate() == null || req.endDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan datos obligatorios");
        }
        LocalDate start = req.startDate();
        LocalDate end = req.endDate();
        if (end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rango de fechas inválido");
        }

        Product product = products.findById(req.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        User user = users.findById(req.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Validar solapamientos por rango con reservas activas
        var overlaps = reservations
                .findByProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(product.getId(), end, start);
        if (!overlaps.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El producto ya tiene reservas en ese rango");
        }

        // Validar días ocupados puntuales (ReservationDate)
        boolean blocked = dates.existsByProductIdAndDateBetween(product.getId(), start, end);
        if (blocked) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Hay días no disponibles en el rango");
        }

        // (status por defecto: CONFIRMED)
        Reservation r = new Reservation();
        r.setProduct(product);
        r.setUser(user);
        r.setStartDate(start);
        r.setEndDate(end);
        r.setStatus(ReservationStatus.CONFIRMED);
        r = reservations.save(r);

        // Materializar días (uno por día) en ReservationDate
        List<ReservationDate> toSave = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            ReservationDate rd = new ReservationDate();
            rd.setDate(d);
            rd.setProduct(product);
            rd.setUser(user);
            // rd.setReservation(r);
            toSave.add(rd);
        }
        dates.saveAll(toSave);

        // Enviar correo de confirmación con los datos de la reserva
        try {
            emailService.sendReservationEmail(
                    user.getEmail(),
                    user.getFirstName() + " " + user.getLastName(),
                    product.getName(),
                    r.getStartDate().toString(),
                    r.getEndDate().toString(),
                    r.getStatus().name());
        } catch (MessagingException e) {
            System.err.println("No se pudo enviar el correo: " + e.getMessage());
        }

        return new ReservationSummaryResponse(
                r.getId(),
                product.getId(),
                r.getProduct().getName(),
                user.getId(),
                r.getStartDate(),
                r.getEndDate(),
                r.getStatus().name(),
                "Reserva creada exitosamente",
                r.getNotes(),
                r.getPassengers());
    }

    public List<ReservationSummaryResponse> getReservationsByUser(Long userId) {
        var reservationsList = reservations.findByUserIdOrderByStartDateDesc(userId);

        return reservationsList.stream().map(r -> new ReservationSummaryResponse(
                r.getId(),
                r.getProduct().getId(),
                r.getProduct().getName(),
                r.getUser().getId(),
                r.getStartDate(),
                r.getEndDate(),
                r.getStatus().name(),
                "Reserva de " + r.getProduct().getName(),
                r.getNotes(),
                r.getPassengers())).toList();
    }
}