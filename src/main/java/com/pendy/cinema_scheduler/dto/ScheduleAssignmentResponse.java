package com.pendy.cinema_scheduler.dto;

import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleAssignmentResponse {

    private ScheduleAssignment data;

    private List<String> warnings;
}