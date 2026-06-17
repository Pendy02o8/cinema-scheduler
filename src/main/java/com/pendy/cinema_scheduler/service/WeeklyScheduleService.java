package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import com.pendy.cinema_scheduler.repository.AvailabilityRepository;
import com.pendy.cinema_scheduler.repository.BusinessHourRepository;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentChangeRepository;
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
    private final AvailabilityRepository availabilityRepository;
    private final BusinessHourRepository businessHourRepository;
    private final ScheduleAssignmentChangeRepository scheduleAssignmentChangeRepository;

    public List<WeeklySchedule> getAllWeeklySchedules() {
        return weeklyScheduleRepository.findAll();
    }

    public WeeklySchedule getWeeklyScheduleById(Long id) {
        return weeklyScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeeklySchedule not found with id: " + id));
    }

    public WeeklySchedule createWeeklySchedule(WeeklySchedule weeklySchedule) {
        if (weeklySchedule.getStatus() == null || weeklySchedule.getStatus().isBlank()) {
            weeklySchedule.setStatus("DRAFT");
        }

        return weeklyScheduleRepository.save(weeklySchedule);
    }

    @Transactional
    public WeeklySchedule updateWeeklySchedule(Long id, WeeklySchedule updatedWeeklySchedule) {

        WeeklySchedule existing = weeklyScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeeklySchedule not found with id: " + id));

        boolean switchingBackToDraft =
                "PUBLISHED".equals(existing.getStatus())
                        && "DRAFT".equals(updatedWeeklySchedule.getStatus());

        existing.setWeekStartDate(updatedWeeklySchedule.getWeekStartDate());
        existing.setWeekEndDate(updatedWeeklySchedule.getWeekEndDate());
        existing.setStatus(updatedWeeklySchedule.getStatus());

        if (switchingBackToDraft) {
            scheduleAssignmentChangeRepository.deleteByWeeklySchedule_Id(id);
        }

        return weeklyScheduleRepository.save(existing);
    }

    public WeeklySchedule publishWeeklySchedule(Long id) {
        WeeklySchedule existing = weeklyScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeeklySchedule not found with id: " + id));

        existing.setStatus("PUBLISHED");

        return weeklyScheduleRepository.save(existing);
    }

    @Transactional
    public void deleteWeeklySchedule(Long id) {
        scheduleAssignmentChangeRepository.deleteByWeeklySchedule_Id(id);
        scheduleAssignmentRepository.deleteByWeeklySchedule_Id(id);
        availabilityRepository.deleteByWeeklySchedule_Id(id);
        businessHourRepository.deleteByWeeklySchedule_Id(id);
        weeklyScheduleRepository.deleteById(id);
    }
}
