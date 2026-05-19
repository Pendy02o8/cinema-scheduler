package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.entity.Position;
import com.pendy.cinema_scheduler.service.PositionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionsController {

    private final PositionsService positionsService;

    @GetMapping
    public List<Position> getAllPositions(){
        return positionsService.getAllPositions();
    }
}
