package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.BusinessHour;
import com.pendy.cinema_scheduler.repository.BusinessHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessHourService {

    private final BusinessHourRepository businessHourRepository;

    public List<BusinessHour> getAllBusinessHours() {
        return businessHourRepository.findAll();
    }
}