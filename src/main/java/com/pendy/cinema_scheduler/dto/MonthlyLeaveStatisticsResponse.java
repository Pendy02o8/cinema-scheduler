package com.pendy.cinema_scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MonthlyLeaveStatisticsResponse {

    private Long employeeId;

    private LocalDate startDate;

    private LocalDate endDate;

    private int regularLeaveDays;

    private int annualLeaveDays;

    private int totalLeaveDays;
}
