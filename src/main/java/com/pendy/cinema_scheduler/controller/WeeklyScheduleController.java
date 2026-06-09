package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import com.pendy.cinema_scheduler.service.WeeklyScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weeklySchedule")
@RequiredArgsConstructor
public class WeeklyScheduleController {

    private final WeeklyScheduleService weeklyScheduleService;

    @GetMapping
    public List<WeeklySchedule> getAllWeeklySchedules() {
        return weeklyScheduleService.getAllWeeklySchedules();
    }

    @GetMapping("/{id}")
    public WeeklySchedule getWeeklyScheduleById(@PathVariable Long id) {
        return weeklyScheduleService.getWeeklyScheduleById(id);
    }

    @PostMapping
    public WeeklySchedule createWeeklySchedule(@RequestBody WeeklySchedule weeklySchedule) {
        return weeklyScheduleService.createWeeklySchedule(weeklySchedule);
    }

    @PutMapping("/{id}")
    public WeeklySchedule updateWeeklySchedule(
            @PathVariable Long id,
            @RequestBody WeeklySchedule weeklySchedule
    ) {
        return weeklyScheduleService.updateWeeklySchedule(id, weeklySchedule);
    }

    @DeleteMapping("/{id}")
    public void deleteWeeklySchedule(@PathVariable Long id) {
        weeklyScheduleService.deleteWeeklySchedule(id);
    }
}