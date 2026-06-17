package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.ScheduleAssignmentChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleAssignmentChangeRepository extends JpaRepository<ScheduleAssignmentChange, Long> {

    List<ScheduleAssignmentChange> findByWeeklySchedule_Id(Long weeklyScheduleId);

    boolean existsByWeeklySchedule_IdAndEmployee_IdAndDate(
            Long weeklyScheduleId,
            Long employeeId,
            LocalDate date
    );

    void deleteByWeeklySchedule_Id(Long weeklyScheduleId);
}
