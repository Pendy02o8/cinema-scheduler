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

    private final WeeklyScheduleService WeeklyScheduleService;

    @GetMapping
    public List<WeeklySchedule> getAllWeeklySchedule(){
        return WeeklyScheduleService.getAllWeeklySchedule();
    }
}
