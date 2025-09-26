package com.laura.easyflights.dto;

import java.time.LocalDate;

public record AvailabilityRequest(Long productId, LocalDate startDate, LocalDate endDate) {}
