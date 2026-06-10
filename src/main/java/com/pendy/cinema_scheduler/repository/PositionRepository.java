package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}