package com.laura.easyflights.dto;

import java.time.LocalDate;
import java.util.List;

public record AvailabilityResponse(boolean available, List<LocalDate> blockedDays, String mes){}
