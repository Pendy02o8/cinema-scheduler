package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.dto.MonthlyLeaveSummaryResponse;
import com.pendy.cinema_scheduler.entity.MonthlyLeave;
import com.pendy.cinema_scheduler.service.MonthlyLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/monthly-leaves")
@RequiredArgsConstructor
public class MonthlyLeaveController {

    private final MonthlyLeaveService monthlyLeaveService;

    @GetMapping
    public List<MonthlyLeave> getAllMonthlyLeaves() {
        return monthlyLeaveService.getAllMonthlyLeaves();
    }

    @GetMapping("/employee/{employeeId}")
    public List<MonthlyLeave> getMonthlyLeavesByEmployeeId(@PathVariable Long employeeId) {
        return monthlyLeaveService.getMonthlyLeavesByEmployeeId(employeeId);
    }

    @GetMapping("/range")
    public List<MonthlyLeave> getMonthlyLeavesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return monthlyLeaveService.getMonthlyLeavesByDateRange(startDate, endDate);
    }

    @PostMapping
    public MonthlyLeave createMonthlyLeave(@RequestBody MonthlyLeave monthlyLeave) {
        return monthlyLeaveService.createMonthlyLeave(monthlyLeave);
    }

    @DeleteMapping("/{id}")
    public void deleteMonthlyLeave(@PathVariable Long id) {
        monthlyLeaveService.deleteMonthlyLeave(id);
    }

    @GetMapping("/summary")
    public List<MonthlyLeaveSummaryResponse> getMonthlyLeaveSummary(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return monthlyLeaveService.getMonthlyLeaveSummary(year, month);
    }
}