package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import com.pendy.cinema_scheduler.repository.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyScheduleService {

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    public List<WeeklySchedule> getAllWeeklySchedules() {
        return weeklyScheduleRepository.findAll();
    }

    public WeeklySchedule getWeeklyScheduleById(Long id) {
        return weeklyScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeeklySchedule not found with id: " + id));
    }

    public WeeklySchedule createWeeklySchedule(WeeklySchedule weeklySchedule) {
        return weeklyScheduleRepository.save(weeklySchedule);
    }

    public WeeklySchedule updateWeeklySchedule(Long id, WeeklySchedule updatedWeeklySchedule) {

        WeeklySchedule existing = weeklyScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeeklySchedule not found with id: " + id));

        existing.setWeekStartDate(updatedWeeklySchedule.getWeekStartDate());
        existing.setWeekEndDate(updatedWeeklySchedule.getWeekEndDate());
        existing.setStatus(updatedWeeklySchedule.getStatus());

        return weeklyScheduleRepository.save(existing);
    }

    public void deleteWeeklySchedule(Long id) {
        if (!weeklyScheduleRepository.existsById(id)) {
            throw new RuntimeException("WeeklySchedule not found with id: " + id);
        }

        weeklyScheduleRepository.deleteById(id);
    }
}