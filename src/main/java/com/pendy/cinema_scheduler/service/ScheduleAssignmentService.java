package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleAssignmentService {

    private final ScheduleAssignmentRepository scheduleAssignmentRepository;

    public List<ScheduleAssignment> getAllScheduleAssignments() {
        return scheduleAssignmentRepository.findAll();
    }

    public ScheduleAssignment getScheduleAssignmentById(Long id) {
        return scheduleAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到排班資料"));
    }

    public ScheduleAssignment createScheduleAssignment(ScheduleAssignment scheduleAssignment) {
        Long employeeId = scheduleAssignment.getEmployee().getId();

        List<ScheduleAssignment> conflicts = scheduleAssignmentRepository.findByEmployee_IdAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                employeeId,scheduleAssignment.getDate(),scheduleAssignment.getEndTime(),scheduleAssignment.getStartTime());

        if(!conflicts.isEmpty()){
            throw new RuntimeException("此員工在該時段已有排班，不能重複排班");
        }
        return scheduleAssignmentRepository.save(scheduleAssignment);
    }

    public ScheduleAssignment updateScheduleAssignment(Long id, ScheduleAssignment newScheduleAssignment) {
        ScheduleAssignment scheduleAssignment = getScheduleAssignmentById(id);

        scheduleAssignment.setWeeklySchedule(newScheduleAssignment.getWeeklySchedule());
        scheduleAssignment.setEmployee(newScheduleAssignment.getEmployee());
        scheduleAssignment.setPosition(newScheduleAssignment.getPosition());
        scheduleAssignment.setDate(newScheduleAssignment.getDate());
        scheduleAssignment.setStartTime(newScheduleAssignment.getStartTime());
        scheduleAssignment.setEndTime(newScheduleAssignment.getEndTime());
        scheduleAssignment.setNote(newScheduleAssignment.getNote());

        return scheduleAssignmentRepository.save(scheduleAssignment);
    }

    public void deleteScheduleAssignment(Long id) {
        scheduleAssignmentRepository.deleteById(id);
    }

    public List<ScheduleAssignment> getScheduleAssignmentsByDate(LocalDate date) {
        return scheduleAssignmentRepository.findByDate(date);
    }

    public List<ScheduleAssignment> getScheduleAssignmentsByEmployeeId(Long employeeId) {
        return scheduleAssignmentRepository.findByEmployeeId(employeeId);
    }

    public List<ScheduleAssignment> getScheduleAssignmentsByPositionId(Long positionId) {
        return scheduleAssignmentRepository.findByPositionId(positionId);
    }
}