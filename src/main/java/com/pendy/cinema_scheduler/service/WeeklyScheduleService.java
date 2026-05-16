package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import com.pendy.cinema_scheduler.repository.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyScheduleService{

    private final WeeklyScheduleRepository WeeklyScheduleRepository;

    public List<WeeklySchedule> getAllWeeklySchedule(){
        return WeeklyScheduleRepository.findAll();
    }
}