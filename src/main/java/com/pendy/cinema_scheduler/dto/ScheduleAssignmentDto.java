package com.pendy.cinema_scheduler.dto;

import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ScheduleAssignmentDto {

    private Long id;
    private Long weeklyScheduleId;
    private Long employeeId;
    private String employeeName;
    private Long positionId;
    private String positionName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String note;

    public static ScheduleAssignmentDto from(ScheduleAssignment assignment) {
        return new ScheduleAssignmentDto(
                assignment.getId(),
                assignment.getWeeklySchedule() == null ? null : assignment.getWeeklySchedule().getId(),
                assignment.getEmployee() == null ? null : assignment.getEmployee().getId(),
                assignment.getEmployee() == null ? null : assignment.getEmployee().getName(),
                assignment.getPosition() == null ? null : assignment.getPosition().getId(),
                assignment.getPosition() == null ? null : assignment.getPosition().getName(),
                assignment.getDate(),
                assignment.getStartTime(),
                assignment.getEndTime(),
                assignment.getNote()
        );
    }
}
