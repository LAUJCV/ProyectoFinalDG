package com.laura.easyflights.dto;

import java.time.LocalDate;

public record ReservationSummaryResponse(
        Long reservationId,
        Long productId,
        String productName,
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        String message,
        String notes,        // opcional
        Integer passengers   // opcional
) {}
