package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.dto.ScheduleAssignmentDto;
import com.pendy.cinema_scheduler.dto.ScheduleAssignmentResponse;
import com.pendy.cinema_scheduler.dto.ScheduleValidationResponse;
import com.pendy.cinema_scheduler.service.ScheduleAssignmentService;
import com.pendy.cinema_scheduler.dto.ScheduleAssignmentRequest;
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
    public List<ScheduleAssignmentDto> getAllScheduleAssignments() {
        return scheduleAssignmentService.getAllScheduleAssignments()
                .stream()
                .map(ScheduleAssignmentDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ScheduleAssignmentDto getScheduleAssignmentById(@PathVariable Long id) {
        return ScheduleAssignmentDto.from(scheduleAssignmentService.getScheduleAssignmentById(id));
    }

    @PostMapping
    public ScheduleAssignmentResponse createScheduleAssignment(
            @RequestBody ScheduleAssignmentRequest request
    ) {
        return scheduleAssignmentService.createScheduleAssignment(request);
    }

    @PutMapping("/{id}")
    public ScheduleAssignmentResponse updateScheduleAssignment(
            @PathVariable Long id,
            @RequestBody ScheduleAssignmentRequest request
    ) {
        return scheduleAssignmentService.updateScheduleAssignment(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteScheduleAssignment(@PathVariable Long id) {
        scheduleAssignmentService.deleteScheduleAssignment(id);
        return "刪除成功";
    }

    @GetMapping("/date/{date}")
    public List<ScheduleAssignmentDto> getScheduleAssignmentsByDate(@PathVariable LocalDate date) {
        return scheduleAssignmentService.getScheduleAssignmentsByDate(date)
                .stream()
                .map(ScheduleAssignmentDto::from)
                .toList();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleAssignmentDto> getScheduleAssignmentsByEmployeeId(@PathVariable Long employeeId) {
        return scheduleAssignmentService.getScheduleAssignmentsByEmployeeId(employeeId)
                .stream()
                .map(ScheduleAssignmentDto::from)
                .toList();
    }

    @GetMapping("/position/{positionId}")
    public List<ScheduleAssignmentDto> getScheduleAssignmentsByPositionId(@PathVariable Long positionId) {
        return scheduleAssignmentService.getScheduleAssignmentsByPositionId(positionId)
                .stream()
                .map(ScheduleAssignmentDto::from)
                .toList();
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
    public List<ScheduleAssignmentDto> getScheduleAssignmentsByWeek(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return scheduleAssignmentService.getScheduleAssignmentsByWeek(
                startDate,
                endDate
        ).stream()
                .map(ScheduleAssignmentDto::from)
                .toList();
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
    @PostMapping("/generate-fixed")
    public List<ScheduleAssignmentDto> generateFixedSchedule(
            @RequestParam Long weeklyScheduleId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return scheduleAssignmentService.generateFixedSchedule(
                weeklyScheduleId,
                startDate,
                endDate
        ).stream()
                .map(ScheduleAssignmentDto::from)
                .toList();
    }
    @PostMapping("/validate")
    public ScheduleValidationResponse validateScheduleAssignment(
            @RequestBody ScheduleAssignmentRequest request
    ) {
        return scheduleAssignmentService.validateScheduleAssignment(request);
    }
}
