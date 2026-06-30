package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.dto.EmployeeRequest;
import com.pendy.cinema_scheduler.dto.EmployeeSortOrderRequest;
import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.repository.AvailabilityRepository;
import com.pendy.cinema_scheduler.repository.EmployeeRepository;
import com.pendy.cinema_scheduler.repository.MonthlyLeaveRepository;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ScheduleAssignmentRepository scheduleAssignmentRepository;
    private final MonthlyLeaveRepository monthlyLeaveRepository;
    private final AvailabilityRepository availabilityRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public Employee getEmplyeeById(Long id){
        return employeeRepository.findById(id).orElseThrow(()->new RuntimeException("找不到員工"));
    }

    public Employee createEmployee(EmployeeRequest request) {
        Employee employee = new Employee();

        employee.setName(request.getName());
        employee.setJobTitle(request.getJobTitle());
        employee.setNote(request.getNote());
        employee.setIsActive(request.getIsActive());
        employee.setEmployeeType(request.getEmployeeType());
        employee.setFixedShiftType(request.getFixedShiftType());
        employee.setSortOrder(request.getSortOrder());

        employee.setRequiresPositionAssignment(
                request.getRequiresPositionAssignment() == null
                        ? true
                        : request.getRequiresPositionAssignment()
        );

        employee.setRequiresMonthlyLeave(
                request.getRequiresMonthlyLeave() == null
                        ? false
                        : request.getRequiresMonthlyLeave()
        );

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, EmployeeRequest request){
        Employee employee = getEmplyeeById(id);

        employee.setName(request.getName());
        employee.setJobTitle(request.getJobTitle());
        employee.setNote(request.getNote());
        employee.setIsActive(request.getIsActive());
        employee.setEmployeeType(request.getEmployeeType());
        employee.setFixedShiftType(request.getFixedShiftType());
        employee.setSortOrder(request.getSortOrder());
        employee.setRequiresPositionAssignment(
                request.getRequiresPositionAssignment() == null
                        ? true
                        : request.getRequiresPositionAssignment()
        );
        employee.setRequiresMonthlyLeave(
                request.getRequiresMonthlyLeave() == null
                        ? false
                        : request.getRequiresMonthlyLeave()
        );
        return employeeRepository.save(employee);
    }

    @Transactional
    public List<Employee> updateSortOrders(List<EmployeeSortOrderRequest> requests) {
        for (EmployeeSortOrderRequest request : requests) {
            Employee employee = employeeRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found: " + request.getId()));
            employee.setSortOrder(request.getSortOrder() == null ? 9999 : request.getSortOrder());
        }

        return employeeRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到員工"));

        boolean hasScheduleAssignments = scheduleAssignmentRepository.existsByEmployee_Id(id);
        boolean hasMonthlyLeaves = monthlyLeaveRepository.existsByEmployee_Id(id);
        boolean hasAvailabilities = availabilityRepository.existsByEmployee_Id(id);

        if (hasScheduleAssignments || hasMonthlyLeaves || hasAvailabilities) {
            throw new RuntimeException("此員工已有班表、假表或可上班時段資料，無法刪除。請改用停用員工。");
        }

        employeeRepository.delete(employee);
    }
}
