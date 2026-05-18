package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmplyeeById(Long id){
        return employeeRepository.findById(id).orElseThrow(()->new RuntimeException("找不到員工"));
    }

    public Employee createEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id,Employee newEmployee){
        Employee employee = getEmplyeeById(id);

        employee.setName(newEmployee.getName());
        employee.setJobTitle(newEmployee.getJobTitle());
        employee.setNote(newEmployee.getNote());
        employee.setIsActive(newEmployee.getIsActive());

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }
}