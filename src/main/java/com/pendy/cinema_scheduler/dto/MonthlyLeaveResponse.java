package com.pendy.cinema_scheduler.dto;

import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.entity.LeaveType;
import com.pendy.cinema_scheduler.entity.MonthlyLeave;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MonthlyLeaveResponse {

    private Long id;

    private Employee employee;

    private Long employeeId;

    private String employeeName;

    private String jobTitle;

    private LocalDate leaveDate;

    private LeaveType leaveType;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static MonthlyLeaveResponse from(MonthlyLeave monthlyLeave) {
        Employee employee = monthlyLeave.getEmployee();
        return new MonthlyLeaveResponse(
                monthlyLeave.getId(),
                employee,
                employee == null ? null : employee.getId(),
                employee == null ? null : employee.getName(),
                employee == null ? null : employee.getJobTitle(),
                monthlyLeave.getLeaveDate(),
                monthlyLeave.getEffectiveLeaveType(),
                monthlyLeave.getNote(),
                monthlyLeave.getCreatedAt(),
                monthlyLeave.getUpdatedAt()
        );
    }
}
