package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}