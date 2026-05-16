package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.entity.PositionRequirement;
import com.pendy.cinema_scheduler.service.PositionRequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/position-requirements")
@RequiredArgsConstructor
public class PositionRequirementController {

    private final PositionRequirementService positionRequirementService;

    @GetMapping
    public List<PositionRequirement> getAllPositionRequirements() {
        return positionRequirementService.getAllPositionRequirements();
    }
}