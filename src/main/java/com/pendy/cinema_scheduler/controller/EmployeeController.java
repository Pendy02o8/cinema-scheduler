package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.dto.EmployeeSortOrderRequest;
import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id){
        return employeeService.getEmplyeeById(id);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.createEmployee(employee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id,@RequestBody Employee employee){
        return employeeService.updateEmployee(id,employee);
    }

    @PutMapping("/sort-order")
    public List<Employee> updateSortOrders(@RequestBody List<EmployeeSortOrderRequest> requests) {
        return employeeService.updateSortOrders(requests);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return "刪除成功";
    }
}
