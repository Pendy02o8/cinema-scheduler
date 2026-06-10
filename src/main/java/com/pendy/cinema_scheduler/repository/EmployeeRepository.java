package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByName(String name);
    List<Employee> findByEmployeeTypeIn(List<String> employeeTypes);
}