package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.MonthlyLeave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MonthlyLeaveRepository extends JpaRepository<MonthlyLeave, Long> {

    List<MonthlyLeave> findByEmployee_Id(Long employeeId);

    List<MonthlyLeave> findByLeaveDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByEmployee_IdAndLeaveDate(Long employeeId, LocalDate leaveDate);

    List<MonthlyLeave> findByEmployee_IdAndLeaveDateBetween(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    );
    boolean existsByEmployee_Id(Long employeeId);
}