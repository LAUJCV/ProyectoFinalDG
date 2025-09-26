package com.laura.easyflights.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import com.laura.easyflights.dto.ReservationCreateRequest;
import com.laura.easyflights.dto.ReservationSummaryResponse;
import com.laura.easyflights.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationSummaryResponse create(@RequestBody ReservationCreateRequest req) {
        return service.create(req);
    }

    @GetMapping("/user/{userId}")
    public List<ReservationSummaryResponse> getUserReservations(@PathVariable Long userId) {
        return service.getReservationsByUser(userId);
    }
}