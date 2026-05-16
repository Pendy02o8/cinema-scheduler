package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.PositionRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRequirementRepository extends JpaRepository<PositionRequirement, Long> {
}