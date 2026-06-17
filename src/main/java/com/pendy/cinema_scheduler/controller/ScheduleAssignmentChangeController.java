package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.entity.ScheduleAssignmentChange;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentChangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule-assignment-changes")
@RequiredArgsConstructor
public class ScheduleAssignmentChangeController {

    private final ScheduleAssignmentChangeRepository scheduleAssignmentChangeRepository;

    @GetMapping("/weekly-schedule/{weeklyScheduleId}")
    public List<ScheduleAssignmentChange> getChangesByWeeklyScheduleId(
            @PathVariable Long weeklyScheduleId
    ) {
        return scheduleAssignmentChangeRepository.findByWeeklySchedule_Id(weeklyScheduleId);
    }
}