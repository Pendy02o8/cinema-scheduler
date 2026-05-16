package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Availability;
import com.pendy.cinema_scheduler.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    public List<Availability> getAllAvailability() {
        return availabilityRepository.findAll();
    }
}