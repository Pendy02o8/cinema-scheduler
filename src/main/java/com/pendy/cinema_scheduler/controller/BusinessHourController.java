package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.entity.BusinessHour;
import com.pendy.cinema_scheduler.service.BusinessHourService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businessHours")
@RequiredArgsConstructor
public class BusinessHourController {

    private final BusinessHourService businessHourService;

    @GetMapping
    public List<BusinessHour> getAllBusinessHours() {
        return businessHourService.getAllBusinessHours();
    }
}