package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, Long> {
    Optional<WeeklySchedule> findByWeekStartDate(LocalDate weekStartDate);
}