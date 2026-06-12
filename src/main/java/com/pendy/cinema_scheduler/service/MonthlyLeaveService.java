package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.entity.MonthlyLeave;
import com.pendy.cinema_scheduler.repository.EmployeeRepository;
import com.pendy.cinema_scheduler.repository.MonthlyLeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pendy.cinema_scheduler.dto.MonthlyLeaveSummaryResponse;

import java.time.YearMonth;
import java.util.ArrayList;

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

        if (!Boolean.TRUE.equals(employee.getRequiresMonthlyLeave())) {
            throw new RuntimeException("此員工不需要使用月休功能");
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
    //計算正職休幾天
    public List<MonthlyLeaveSummaryResponse> getMonthlyLeaveSummary(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Employee> employees = employeeRepository.findByRequiresMonthlyLeaveTrueOrderBySortOrderAscIdAsc();

        List<MonthlyLeaveSummaryResponse> result = new ArrayList<>();

        for (Employee employee : employees) {
            List<MonthlyLeave> leaves = monthlyLeaveRepository.findByEmployee_IdAndLeaveDateBetween(
                    employee.getId(),
                    startDate,
                    endDate
            );

            List<LocalDate> leaveDates = leaves.stream()
                    .map(MonthlyLeave::getLeaveDate)
                    .sorted()
                    .toList();

            result.add(new MonthlyLeaveSummaryResponse(
                    employee.getId(),
                    employee.getName(),
                    employee.getJobTitle(),
                    leaveDates.size(),
                    leaveDates
            ));
        }

        return result;
    }

    public void deleteMonthlyLeave(Long id) {
        monthlyLeaveRepository.deleteById(id);
    }

    public boolean isEmployeeOnLeave(Long employeeId, LocalDate date) {
        return monthlyLeaveRepository.existsByEmployee_IdAndLeaveDate(employeeId, date);
    }
}