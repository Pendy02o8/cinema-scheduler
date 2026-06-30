package com.pendy.cinema_scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleAssignmentResponse {

    private ScheduleAssignmentDto data;

    private List<String> warnings;
}
