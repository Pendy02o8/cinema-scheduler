package com.pendy.cinema_scheduler.repository;

import com.pendy.cinema_scheduler.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionsRepository extends JpaRepository<Position, Long> {
}