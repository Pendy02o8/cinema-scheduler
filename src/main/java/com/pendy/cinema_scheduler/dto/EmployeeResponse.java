package com.pendy.cinema_scheduler.dto;

import com.pendy.cinema_scheduler.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String jobTitle;
    private Boolean isActive;
    private String note;
    private String employeeType;
    private String fixedShiftType;
    private Integer sortOrder;
    private Boolean requiresPositionAssignment;
    private Boolean requiresMonthlyLeave;

    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getJobTitle(),
                employee.getIsActive(),
                employee.getNote(),
                employee.getEmployeeType(),
                employee.getFixedShiftType(),
                employee.getSortOrder(),
                employee.getRequiresPositionAssignment(),
                employee.getRequiresMonthlyLeave()
        );
    }
}