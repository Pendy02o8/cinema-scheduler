package com.pendy.cinema_scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlyLeaveSummaryResponse {

    private Long employeeId;

    private String employeeName;

    private String jobTitle;

    private int leaveDays;

    private List<LocalDate> leaveDates;
}