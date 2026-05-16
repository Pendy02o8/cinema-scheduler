package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.PositionRequirement;
import com.pendy.cinema_scheduler.repository.PositionRequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionRequirementService {

    private final PositionRequirementRepository positionRequirementRepository;

    public List<PositionRequirement> getAllPositionRequirements() {
        return positionRequirementRepository.findAll();
    }
}