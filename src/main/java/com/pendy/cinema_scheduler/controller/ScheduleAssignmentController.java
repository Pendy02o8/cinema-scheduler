package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import com.pendy.cinema_scheduler.service.ScheduleAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule-assignments")
@RequiredArgsConstructor
public class ScheduleAssignmentController {

    private final ScheduleAssignmentService scheduleAssignmentService;

    @GetMapping
    public List<ScheduleAssignment> getAllScheduleAssignments() {
        return scheduleAssignmentService.getAllScheduleAssignments();
    }

    @GetMapping("/{id}")
    public ScheduleAssignment getScheduleAssignmentById(@PathVariable Long id) {
        return scheduleAssignmentService.getScheduleAssignmentById(id);
    }

    @PostMapping
    public ScheduleAssignment createScheduleAssignment(@RequestBody ScheduleAssignment scheduleAssignment) {
        return scheduleAssignmentService.createScheduleAssignment(scheduleAssignment);
    }

    @PutMapping("/{id}")
    public ScheduleAssignment updateScheduleAssignment(
            @PathVariable Long id,
            @RequestBody ScheduleAssignment scheduleAssignment
    ) {
        return scheduleAssignmentService.updateScheduleAssignment(id, scheduleAssignment);
    }

    @DeleteMapping("/{id}")
    public String deleteScheduleAssignment(@PathVariable Long id) {
        scheduleAssignmentService.deleteScheduleAssignment(id);
        return "刪除成功";
    }

    @GetMapping("/date/{date}")
    public List<ScheduleAssignment> getScheduleAssignmentsByDate(@PathVariable LocalDate date) {
        return scheduleAssignmentService.getScheduleAssignmentsByDate(date);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleAssignment> getScheduleAssignmentsByEmployeeId(@PathVariable Long employeeId) {
        return scheduleAssignmentService.getScheduleAssignmentsByEmployeeId(employeeId);
    }

    @GetMapping("/position/{positionId}")
    public List<ScheduleAssignment> getScheduleAssignmentsByPositionId(@PathVariable Long positionId) {
        return scheduleAssignmentService.getScheduleAssignmentsByPositionId(positionId);
    }

    @GetMapping("/check-gaps/{date}")
    public List<String> checkGaps(@PathVariable LocalDate date) {
        return scheduleAssignmentService.checkGaps(date);
    }

    @GetMapping("/check-overstaffed/{date}")
    public List<String> checkOverstaffed(@PathVariable LocalDate date) {
        return scheduleAssignmentService.checkOverstaffed(date);
    }

    @GetMapping("/work-hours/employee/{employeeId}")
    public String getEmployeeWorkHours( @PathVariable Long employeeId, @RequestParam LocalDate startDate,@RequestParam LocalDate endDate) {
        return scheduleAssignmentService.getEmployeeWorkHours(employeeId, startDate,endDate);
    }
    @GetMapping("/work-hours/all")
    public List<String> getAllEmployeesWorkHours(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return scheduleAssignmentService.getAllEmployeesWorkHours(
                startDate,
                endDate
        );
    }
    @GetMapping("/check-schedule/{date}")
    public List<String> checkSchedule(@PathVariable LocalDate date) {
        return scheduleAssignmentService.checkSchedule(date);
    }
    @GetMapping("/week")
    public List<ScheduleAssignment> getScheduleAssignmentsByWeek(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return scheduleAssignmentService.getScheduleAssignmentsByWeek(
                startDate,
                endDate
        );
    }
    @GetMapping("/check-schedule/week")
    public List<String> checkScheduleByWeek(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return scheduleAssignmentService.checkScheduleByWeek(
                startDate,
                endDate
        );
    }
}