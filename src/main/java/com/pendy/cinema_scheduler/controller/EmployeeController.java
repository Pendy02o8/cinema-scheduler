package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.dto.EmployeeRequest;
import com.pendy.cinema_scheduler.dto.EmployeeResponse;
import com.pendy.cinema_scheduler.dto.EmployeeSortOrderRequest;
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
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees()
                .stream()
                .map(EmployeeResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id){
        return EmployeeResponse.from(employeeService.getEmplyeeById(id));
    }

    @PostMapping
    public EmployeeResponse createEmployee(@RequestBody EmployeeRequest request) {
        return EmployeeResponse.from(employeeService.createEmployee(request));
    }

    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequest request
    ) {
        return EmployeeResponse.from(employeeService.updateEmployee(id, request));
    }

    @PutMapping("/sort-order")
    public List<EmployeeResponse> updateSortOrders(@RequestBody List<EmployeeSortOrderRequest> requests) {
        return employeeService.updateSortOrders(requests)
                .stream()
                .map(EmployeeResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return "刪除成功";
    }
}
