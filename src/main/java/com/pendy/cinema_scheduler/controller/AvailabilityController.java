package com.pendy.cinema_scheduler.controller;

import com.pendy.cinema_scheduler.entity.Availability;
import com.pendy.cinema_scheduler.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
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

    @GetMapping("/{id}")
    public Availability getAvailabilityById(@PathVariable Long id){
        return availabilityService.getAvailabilityById(id);
    }

    @PostMapping
    public Availability createAvailability(@RequestBody Availability availability){
        return availabilityService.createAvailability(availability);
    }

    @PutMapping("/{id}")
    public Availability updateAvailability(@PathVariable Long id,
                                           @RequestBody Availability availability){
        return availabilityService.updateAvailability(id,availability);
    }

    @DeleteMapping("/{id}")
    public String deleteAvailability(@PathVariable Long id){
        availabilityService.deleteAvailablity(id);
        return "刪除成功";
    }

    @GetMapping("/employee/{employeeId}")
    public List<Availability> getAvailabityByEmployeeId(@PathVariable Long employeeId){
        return availabilityService.getAvailabilityByEmployeeId(employeeId);
    }
}