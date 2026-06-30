package com.pendy.cinema_scheduler.dto;

import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.entity.Position;
import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ScheduleAssignmentRequest {

    private Long weeklyScheduleId;
    private Long employeeId;
    private Long positionId;

    private WeeklySchedule weeklySchedule;
    private Employee employee;
    private Position position;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private String note;
}
