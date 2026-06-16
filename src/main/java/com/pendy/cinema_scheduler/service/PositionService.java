package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Position;
import com.pendy.cinema_scheduler.repository.PositionRepository;
import com.pendy.cinema_scheduler.repository.PositionRequirementRepository;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final ScheduleAssignmentRepository scheduleAssignmentRepository;
    private final PositionRequirementRepository positionRequirementRepository;

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
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到崗位"));

        boolean hasScheduleAssignments = scheduleAssignmentRepository.existsByPosition_Id(id);
        boolean hasPositionRequirements = positionRequirementRepository.existsByPosition_Id(id);

        if (hasScheduleAssignments || hasPositionRequirements) {
            throw new RuntimeException("此崗位已有班表或需求設定資料，無法刪除。");
        }
        positionRepository.delete(position);
    }
}