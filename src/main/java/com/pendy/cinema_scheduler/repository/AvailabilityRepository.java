package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
}