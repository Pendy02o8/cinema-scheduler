package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Position;
import com.pendy.cinema_scheduler.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Position getPositionById(Long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到崗位 id: " + id));
    }

    public Position createPosition(Position position) {
        position.setCreatedAt(LocalDateTime.now());
        position.setUpdatedAt(LocalDateTime.now());

        return positionRepository.save(position);
    }

    public Position updatePosition(Long id, Position newPosition) {
        Position position = getPositionById(id);

        position.setName(newPosition.getName());
        position.setIsRequired(newPosition.getIsRequired());
        position.setUpdatedAt(LocalDateTime.now());

        return positionRepository.save(position);
    }

    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }
}