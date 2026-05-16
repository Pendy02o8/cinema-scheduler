package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.entity.Availability;
import com.pendy.cinema_scheduler.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @GetMapping
    public List<Availability> getAllAvailability() {
        return availabilityService.getAllAvailability();
    }
}