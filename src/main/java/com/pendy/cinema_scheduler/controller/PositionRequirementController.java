package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.entity.PositionRequirement;
import com.pendy.cinema_scheduler.service.PositionRequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/{id}")
    public PositionRequirement getPositionRequirementById(@PathVariable Long id){
        return positionRequirementService.getPositionRequirementById(id);
    }

    @PostMapping
    public PositionRequirement createPositionRequirement(@RequestBody PositionRequirement positionRequirement){
        return positionRequirementService.createPositionRequirement(positionRequirement);
    }

    @PutMapping("/{id}")
    public PositionRequirement updatePositionRequirement(@PathVariable Long id,
                                                         @RequestBody PositionRequirement positionRequirement){
           return positionRequirementService.updatePositionRequirement(id,positionRequirement);
    }

    @DeleteMapping("/{id}")
    public String deletePositionRequirement(@PathVariable Long id){
        positionRequirementService.deletePositionRequirement(id);
        return "刪除成功";
    }

    @GetMapping("/date/{date}")
    public List<PositionRequirement> getPositionRequirementByDate(@PathVariable LocalDate date){
        return positionRequirementService.getPositionRequirementByDate(date);
    }

    @GetMapping("/position/{positionId}")
    public List<PositionRequirement> getPositionRequirementByPositionId(@PathVariable Long positionId){
        return positionRequirementService.getPositionRequirementByPositionId(positionId);
    }
}