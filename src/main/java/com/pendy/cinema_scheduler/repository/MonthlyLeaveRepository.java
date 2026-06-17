package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.LeaveType;
import com.pendy.cinema_scheduler.entity.MonthlyLeave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MonthlyLeaveRepository extends JpaRepository<MonthlyLeave, Long> {

    List<MonthlyLeave> findByEmployee_Id(Long employeeId);

    List<MonthlyLeave> findByLeaveDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByEmployee_IdAndLeaveDate(Long employeeId, LocalDate leaveDate);

    boolean existsByEmployee_IdAndLeaveDateAndIdNot(Long employeeId, LocalDate leaveDate, Long id);

    Optional<MonthlyLeave> findByEmployee_IdAndLeaveDate(Long employeeId, LocalDate leaveDate);

    List<MonthlyLeave> findByEmployee_IdAndLeaveDateBetween(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    );

    int countByEmployee_IdAndLeaveType(Long employeeId, LeaveType leaveType);

    int countByEmployee_IdAndLeaveTypeAndLeaveDateBetween(
            Long employeeId,
            LeaveType leaveType,
            LocalDate startDate,
            LocalDate endDate
    );

    boolean existsByEmployee_Id(Long employeeId);
}
