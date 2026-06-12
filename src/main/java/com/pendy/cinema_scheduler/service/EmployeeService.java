package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.dto.EmployeeSortOrderRequest;
import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public Employee getEmplyeeById(Long id){
        return employeeRepository.findById(id).orElseThrow(()->new RuntimeException("找不到員工"));
    }

    public Employee createEmployee(Employee employee){
        if (employee.getRequiresPositionAssignment() == null) {
            employee.setRequiresPositionAssignment(true);
        }
        if (employee.getRequiresMonthlyLeave() == null) {
            employee.setRequiresMonthlyLeave(false);
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id,Employee newEmployee){
        Employee employee = getEmplyeeById(id);

        employee.setName(newEmployee.getName());
        employee.setJobTitle(newEmployee.getJobTitle());
        employee.setNote(newEmployee.getNote());
        employee.setIsActive(newEmployee.getIsActive());
        employee.setSortOrder(newEmployee.getSortOrder());
        employee.setRequiresPositionAssignment(
                newEmployee.getRequiresPositionAssignment() == null
                        ? true
                        : newEmployee.getRequiresPositionAssignment()
        );
        employee.setRequiresMonthlyLeave(
                newEmployee.getRequiresMonthlyLeave() == null
                        ? false
                        : newEmployee.getRequiresMonthlyLeave()
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

    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }
}
