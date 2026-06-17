package com.pendy.cinema_scheduler.dto;

import com.pendy.cinema_scheduler.entity.LeaveType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MonthlyLeaveUpdateRequest {

    private Long employeeId;

    private LocalDate leaveDate;

    private LeaveType leaveType;

    private String note;
}
