package com.pendy.cinema_scheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequest {
    private String name;
    private String jobTitle;
    private Boolean isActive;
    private String note;
    private String employeeType;
    private String fixedShiftType;
    private Integer sortOrder;
    private Boolean requiresPositionAssignment;
    private Boolean requiresMonthlyLeave;
}