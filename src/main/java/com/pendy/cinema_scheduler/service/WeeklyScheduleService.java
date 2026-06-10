package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentRepository;
import com.pendy.cinema_scheduler.repository.WeeklyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyScheduleService {

    private final WeeklyScheduleRepository weeklyScheduleRepository;
    private final ScheduleAssignmentRepository scheduleAssignmentRepository;

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

    @Transactional
    public void deleteWeeklySchedule(Long id) {
        scheduleAssignmentRepository.deleteByWeeklySchedule_Id(id);
        weeklyScheduleRepository.deleteById(id);
    }
}