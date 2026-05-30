package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.PositionRequirement;
import com.pendy.cinema_scheduler.repository.PositionRequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionRequirementService {

    private final PositionRequirementRepository positionRequirementRepository;

    public List<PositionRequirement> getAllPositionRequirements() {
        return positionRequirementRepository.findAll();
    }

    public PositionRequirement getPositionRequirementById(Long id){
        return positionRequirementRepository.findById(id).orElseThrow(()->new RuntimeException("找不到職位需求"));
    }

    public PositionRequirement createPositionRequirement(PositionRequirement positionRequirement){
        System.out.println("positionRequirement = " + positionRequirement);
        System.out.println("position = " + positionRequirement.getPosition());

        if (positionRequirement.getPosition() != null) {
            System.out.println("position id = " + positionRequirement.getPosition().getId());
        }

        return positionRequirementRepository.save(positionRequirement);
    }

    public PositionRequirement updatePositionRequirement(Long id,PositionRequirement newPositionRequirement){
        PositionRequirement positionRequirement = getPositionRequirementById(id);

        positionRequirement.setStartTime(newPositionRequirement.getStartTime());
        positionRequirement.setEndTime(newPositionRequirement.getEndTime());
        positionRequirement.setPosition(newPositionRequirement.getPosition());
        positionRequirement.setRequiredCount(newPositionRequirement.getRequiredCount());

        return positionRequirementRepository.save(positionRequirement);
    }

    public void deletePositionRequirement(Long id){
        positionRequirementRepository.deleteById(id);
    }

    public List<PositionRequirement> getPositionRequirementByPositionId(Long positionId){
        return positionRequirementRepository.findByPositionId(positionId);
    }
}