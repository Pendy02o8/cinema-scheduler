package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.PositionRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;

public interface PositionRequirementRepository extends JpaRepository<PositionRequirement, Long> {
    List<PositionRequirement> findAll();
    List<PositionRequirement> findByPositionId(Long positionId);
}