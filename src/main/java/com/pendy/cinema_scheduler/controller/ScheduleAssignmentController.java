package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import com.pendy.cinema_scheduler.service.ScheduleAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule-assignments")
@RequiredArgsConstructor
public class ScheduleAssignmentController {

    private final ScheduleAssignmentService scheduleAssignmentService;

    @GetMapping
    public List<ScheduleAssignment> getAllScheduleAssignments() {
        return scheduleAssignmentService.getAllScheduleAssignments();
    }
}