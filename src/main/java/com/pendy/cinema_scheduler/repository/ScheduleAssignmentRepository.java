package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleAssignmentRepository extends JpaRepository<ScheduleAssignment, Long> {
}