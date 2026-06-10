package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.BusinessHour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {
    void deleteByWeeklySchedule_Id(Long weeklyScheduleId);
}