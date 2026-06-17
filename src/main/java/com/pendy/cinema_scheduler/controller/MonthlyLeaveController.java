package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.dto.MonthlyLeaveCreateRequest;
import com.pendy.cinema_scheduler.dto.MonthlyLeaveResponse;
import com.pendy.cinema_scheduler.dto.MonthlyLeaveStatisticsResponse;
import com.pendy.cinema_scheduler.dto.MonthlyLeaveSummaryResponse;
import com.pendy.cinema_scheduler.dto.MonthlyLeaveUpdateRequest;
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
    public List<MonthlyLeaveResponse> getAllMonthlyLeaves() {
        return monthlyLeaveService.getAllMonthlyLeaves();
    }

    @GetMapping("/{id}")
    public MonthlyLeaveResponse getMonthlyLeaveById(@PathVariable Long id) {
        return monthlyLeaveService.getMonthlyLeaveById(id);
    }

    @GetMapping("/employee/{employeeId}")
    public List<MonthlyLeaveResponse> getMonthlyLeavesByEmployeeId(@PathVariable Long employeeId) {
        return monthlyLeaveService.getMonthlyLeavesByEmployeeId(employeeId);
    }

    @GetMapping("/range")
    public List<MonthlyLeaveResponse> getMonthlyLeavesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return monthlyLeaveService.getMonthlyLeavesByDateRange(startDate, endDate);
    }

    @PostMapping
    public MonthlyLeaveResponse createMonthlyLeave(@RequestBody MonthlyLeaveCreateRequest request) {
        return monthlyLeaveService.createMonthlyLeave(request);
    }

    @PutMapping("/{id}")
    public MonthlyLeaveResponse updateMonthlyLeave(
            @PathVariable Long id,
            @RequestBody MonthlyLeaveUpdateRequest request
    ) {
        return monthlyLeaveService.updateMonthlyLeave(id, request);
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

    @GetMapping("/statistics")
    public MonthlyLeaveStatisticsResponse getMonthlyLeaveStatistics(
            @RequestParam Long employeeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return monthlyLeaveService.getMonthlyLeaveStatistics(employeeId, startDate, endDate);
    }
}
