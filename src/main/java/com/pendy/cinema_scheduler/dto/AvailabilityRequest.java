package com.pendy.cinema_scheduler.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AvailabilityRequest {
    private Long employeeId;
    private Long weeklyScheduleId;
    private LocalDate date;
    private String availabilityType;
    private LocalTime boundaryTime;
    private String note;
}