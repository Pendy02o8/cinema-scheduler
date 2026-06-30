package com.pendy.cinema_scheduler.dto;

import com.pendy.cinema_scheduler.entity.Availability;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class AvailabilityResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long weeklyScheduleId;
    private LocalDate date;
    private String availabilityType;
    private LocalTime boundaryTime;
    private String note;

    public static AvailabilityResponse from(Availability availability) {
        return new AvailabilityResponse(
                availability.getId(),
                availability.getEmployee() == null ? null : availability.getEmployee().getId(),
                availability.getEmployee() == null ? null : availability.getEmployee().getName(),
                availability.getWeeklySchedule() == null ? null : availability.getWeeklySchedule().getId(),
                availability.getDate(),
                availability.getAvailabilityType(),
                availability.getBoundaryTime(),
                availability.getNote()
        );
    }
}