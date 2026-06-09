package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByEmployee_Id(Long employeeId);

    List<Availability> findByEmployee_IdAndDate(
            Long employeeId,
            LocalDate date
    );
    void deleteByWeeklySchedule_Id(Long weeklyScheduleId);
}