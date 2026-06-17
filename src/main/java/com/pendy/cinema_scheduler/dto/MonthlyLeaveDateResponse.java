package com.pendy.cinema_scheduler.dto;

import com.pendy.cinema_scheduler.entity.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MonthlyLeaveDateResponse {

    private LocalDate leaveDate;

    private LeaveType leaveType;
}
