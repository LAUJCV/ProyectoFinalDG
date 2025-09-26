package com.laura.easyflights.dto;

import java.time.LocalDate;

public record ReservationCreateRequest (
         Long productId,
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        String notes,        // opcional
        Integer passengers   // opcional
){}
