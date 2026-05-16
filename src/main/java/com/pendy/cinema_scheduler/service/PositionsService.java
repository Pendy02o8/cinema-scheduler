package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Positions;
import com.pendy.cinema_scheduler.repository.PositionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionsService{

    private final PositionsRepository positionsRepository;

    public List<Positions> getAllPositions(){
        return positionsRepository.findAll();
    }
}