package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, Long> {
}