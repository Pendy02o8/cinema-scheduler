package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.entity.MonthlyLeave;
import com.pendy.cinema_scheduler.repository.EmployeeRepository;
import com.pendy.cinema_scheduler.repository.MonthlyLeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyLeaveService {

    private final MonthlyLeaveRepository monthlyLeaveRepository;
    private final EmployeeRepository employeeRepository;

    public List<MonthlyLeave> getAllMonthlyLeaves() {
        return monthlyLeaveRepository.findAll();
    }

    public List<MonthlyLeave> getMonthlyLeavesByEmployeeId(Long employeeId) {
        return monthlyLeaveRepository.findByEmployee_Id(employeeId);
    }

    public List<MonthlyLeave> getMonthlyLeavesByDateRange(LocalDate startDate, LocalDate endDate) {
        return monthlyLeaveRepository.findByLeaveDateBetween(startDate, endDate);
    }

    public MonthlyLeave createMonthlyLeave(MonthlyLeave monthlyLeave) {
        Long employeeId = monthlyLeave.getEmployee().getId();

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("找不到員工 id: " + employeeId));

        if ("PART_TIME".equals(employee.getEmployeeType())) {
            throw new RuntimeException("工讀生不可使用月休功能");
        }

        boolean exists = monthlyLeaveRepository.existsByEmployee_IdAndLeaveDate(
                employeeId,
                monthlyLeave.getLeaveDate()
        );

        if (exists) {
            throw new RuntimeException("該員工在這一天已經有休假記錄");
        }

        monthlyLeave.setEmployee(employee);
        monthlyLeave.setCreatedAt(LocalDateTime.now());
        monthlyLeave.setUpdatedAt(LocalDateTime.now());

        return monthlyLeaveRepository.save(monthlyLeave);
    }

    public void deleteMonthlyLeave(Long id) {
        monthlyLeaveRepository.deleteById(id);
    }

    public boolean isEmployeeOnLeave(Long employeeId, LocalDate date) {
        return monthlyLeaveRepository.existsByEmployee_IdAndLeaveDate(employeeId, date);
    }
}