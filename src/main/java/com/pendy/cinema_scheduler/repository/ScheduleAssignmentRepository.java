package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleAssignmentRepository extends JpaRepository<ScheduleAssignment, Long> {
    List<ScheduleAssignment> findByDate(LocalDate date);
    List<ScheduleAssignment> findByEmployeeId(Long employeeId);
    List<ScheduleAssignment> findByPositionId(Long positionId);
    List<ScheduleAssignment> findByEmployee_IdAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
            Long employeeId,
            LocalDate date,
            LocalTime endTime,
            LocalTime startTime
    );
    List<ScheduleAssignment> findByDateAndPosition_Id(
            LocalDate date,
            Long positionId
    );
    List<ScheduleAssignment> findByEmployee_IdAndDateBetween(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    );
    List<ScheduleAssignment> findByDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
    List<ScheduleAssignment> findByEmployee_IdAndDate(Long employeeId, LocalDate date);

    void deleteByWeeklySchedule_Id(Long weeklyScheduleId);

    boolean existsByEmployee_Id(Long employeeId);
}