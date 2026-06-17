package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.dto.MonthlyLeaveCreateRequest;
import com.pendy.cinema_scheduler.dto.MonthlyLeaveDateResponse;
import com.pendy.cinema_scheduler.dto.MonthlyLeaveResponse;
import com.pendy.cinema_scheduler.dto.MonthlyLeaveStatisticsResponse;
import com.pendy.cinema_scheduler.dto.MonthlyLeaveSummaryResponse;
import com.pendy.cinema_scheduler.dto.MonthlyLeaveUpdateRequest;
import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.entity.LeaveType;
import com.pendy.cinema_scheduler.entity.MonthlyLeave;
import com.pendy.cinema_scheduler.repository.EmployeeRepository;
import com.pendy.cinema_scheduler.repository.MonthlyLeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyLeaveService {

    private final MonthlyLeaveRepository monthlyLeaveRepository;
    private final EmployeeRepository employeeRepository;

    public List<MonthlyLeaveResponse> getAllMonthlyLeaves() {
        return monthlyLeaveRepository.findAll().stream()
                .map(MonthlyLeaveResponse::from)
                .toList();
    }

    public MonthlyLeaveResponse getMonthlyLeaveById(Long id) {
        return MonthlyLeaveResponse.from(findMonthlyLeave(id));
    }

    public List<MonthlyLeaveResponse> getMonthlyLeavesByEmployeeId(Long employeeId) {
        return monthlyLeaveRepository.findByEmployee_Id(employeeId).stream()
                .map(MonthlyLeaveResponse::from)
                .toList();
    }

    public List<MonthlyLeaveResponse> getMonthlyLeavesByDateRange(LocalDate startDate, LocalDate endDate) {
        return monthlyLeaveRepository.findByLeaveDateBetween(startDate, endDate).stream()
                .map(MonthlyLeaveResponse::from)
                .toList();
    }

    public MonthlyLeaveResponse createMonthlyLeave(MonthlyLeaveCreateRequest request) {
        validateRequest(request);
        Long employeeId = resolveEmployeeId(request.getEmployeeId());
        Employee employee = findEmployee(employeeId);

        validateEmployeeCanUseMonthlyLeave(employee);
        validateDuplicate(employeeId, request.getLeaveDate(), null);

        LocalDateTime now = LocalDateTime.now();
        MonthlyLeave monthlyLeave = new MonthlyLeave();
        monthlyLeave.setEmployee(employee);
        monthlyLeave.setLeaveDate(request.getLeaveDate());
        monthlyLeave.setLeaveType(resolveLeaveType(request.getLeaveType()));
        monthlyLeave.setNote(request.getNote());
        monthlyLeave.setCreatedAt(now);
        monthlyLeave.setUpdatedAt(now);

        return MonthlyLeaveResponse.from(monthlyLeaveRepository.save(monthlyLeave));
    }

    public MonthlyLeaveResponse updateMonthlyLeave(Long id, MonthlyLeaveUpdateRequest request) {
        validateRequest(request);
        MonthlyLeave monthlyLeave = findMonthlyLeave(id);
        Long employeeId = resolveEmployeeId(request.getEmployeeId());
        Employee employee = findEmployee(employeeId);

        validateEmployeeCanUseMonthlyLeave(employee);
        validateDuplicate(employeeId, request.getLeaveDate(), id);

        monthlyLeave.setEmployee(employee);
        monthlyLeave.setLeaveDate(request.getLeaveDate());
        monthlyLeave.setLeaveType(resolveLeaveType(request.getLeaveType()));
        monthlyLeave.setNote(request.getNote());
        monthlyLeave.setUpdatedAt(LocalDateTime.now());

        return MonthlyLeaveResponse.from(monthlyLeaveRepository.save(monthlyLeave));
    }

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

            List<MonthlyLeaveDateResponse> leaveDetails = leaves.stream()
                    .sorted(Comparator.comparing(MonthlyLeave::getLeaveDate))
                    .map(leave -> new MonthlyLeaveDateResponse(
                            leave.getLeaveDate(),
                            leave.getEffectiveLeaveType()
                    ))
                    .toList();

            List<LocalDate> leaveDates = leaveDetails.stream()
                    .map(MonthlyLeaveDateResponse::getLeaveDate)
                    .toList();

            int regularLeaveDays = countLeaves(employee.getId(), LeaveType.REGULAR_LEAVE, startDate, endDate);
            int annualLeaveDays = countLeaves(employee.getId(), LeaveType.ANNUAL_LEAVE, startDate, endDate);
            int totalLeaveDays = leaves.size();

            result.add(new MonthlyLeaveSummaryResponse(
                    employee.getId(),
                    employee.getName(),
                    employee.getJobTitle(),
                    totalLeaveDays,
                    leaveDates,
                    regularLeaveDays,
                    annualLeaveDays,
                    totalLeaveDays,
                    leaveDetails
            ));
        }

        return result;
    }

    public MonthlyLeaveStatisticsResponse getMonthlyLeaveStatistics(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        int regularLeaveDays = countLeaves(employeeId, LeaveType.REGULAR_LEAVE, startDate, endDate);
        int annualLeaveDays = countLeaves(employeeId, LeaveType.ANNUAL_LEAVE, startDate, endDate);
        return new MonthlyLeaveStatisticsResponse(
                employeeId,
                startDate,
                endDate,
                regularLeaveDays,
                annualLeaveDays,
                regularLeaveDays + annualLeaveDays
        );
    }

    public void deleteMonthlyLeave(Long id) {
        monthlyLeaveRepository.deleteById(id);
    }

    public boolean isEmployeeOnLeave(Long employeeId, LocalDate date) {
        return monthlyLeaveRepository.existsByEmployee_IdAndLeaveDate(employeeId, date);
    }

    public LeaveType getLeaveType(Long employeeId, LocalDate date) {
        return monthlyLeaveRepository.findByEmployee_IdAndLeaveDate(employeeId, date)
                .map(MonthlyLeave::getEffectiveLeaveType)
                .orElse(null);
    }

    private MonthlyLeave findMonthlyLeave(Long id) {
        return monthlyLeaveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Monthly leave not found: " + id
                ));
    }

    private Employee findEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employee not found: " + employeeId
                ));
    }

    private void validateRequest(Object request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required.");
        }
    }

    private Long resolveEmployeeId(Long employeeId) {
        if (employeeId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "employeeId is required.");
        }
        return employeeId;
    }

    private LeaveType resolveLeaveType(LeaveType leaveType) {
        return leaveType == null ? LeaveType.REGULAR_LEAVE : leaveType;
    }

    private void validateEmployeeCanUseMonthlyLeave(Employee employee) {
        if (!Boolean.TRUE.equals(employee.getRequiresMonthlyLeave())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Employee does not use monthly leave tracking."
            );
        }
    }

    private void validateDuplicate(Long employeeId, LocalDate leaveDate, Long currentId) {
        if (leaveDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "leaveDate is required.");
        }

        boolean exists = currentId == null
                ? monthlyLeaveRepository.existsByEmployee_IdAndLeaveDate(employeeId, leaveDate)
                : monthlyLeaveRepository.existsByEmployee_IdAndLeaveDateAndIdNot(employeeId, leaveDate, currentId);

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Monthly leave already exists for this employee and date."
            );
        }
    }

    private int countLeaves(Long employeeId, LeaveType leaveType, LocalDate startDate, LocalDate endDate) {
        return monthlyLeaveRepository.countByEmployee_IdAndLeaveTypeAndLeaveDateBetween(
                employeeId,
                leaveType,
                startDate,
                endDate
        );
    }
}
