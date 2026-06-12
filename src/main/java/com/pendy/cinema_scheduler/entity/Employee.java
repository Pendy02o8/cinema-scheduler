package com.pendy.cinema_scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String jobTitle;

    private Boolean isActive;

    private String note;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    @Column(name = "employee_type")
    private String employeeType;
    // PART_TIME, FULL_TIME, CLEANER

    @Column(name = "fixed_shift_type")
    private String fixedShiftType;
    // MORNING, EVENING, NONE

    @Column(name = "sort_order")
    private Integer sortOrder = 9999;

    @Column(name = "requires_position_assignment")
    private Boolean requiresPositionAssignment = true;

    @Column(name = "requires_monthly_leave")
    private Boolean requiresMonthlyLeave = false;
}