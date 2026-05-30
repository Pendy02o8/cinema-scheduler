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

    public Availability getAvailabilityById(Long id){
        return availabilityRepository.findById(id).orElseThrow(()->new RuntimeException("未提供假表"));
    }

    public Availability createAvailability(Availability availability){
        return availabilityRepository.save(availability);
    }

    public Availability updateAvailability(Long id,Availability newAvailability){
        Availability availability = getAvailabilityById(id);

        availability.setEmployee(newAvailability.getEmployee());
        availability.setWeeklySchedule(newAvailability.getWeeklySchedule());
        availability.setDate(newAvailability.getDate());
        availability.setAvailabilityType(newAvailability.getAvailabilityType());
        availability.setBoundaryTime(newAvailability.getBoundaryTime());
        availability.setNote(newAvailability.getNote());

        return availabilityRepository.save(availability);
    }

    public void deleteAvailablity(Long id){
        availabilityRepository.deleteById(id);
    }

    public List<Availability> getAvailabilityByEmployeeId(Long employeeId){
        return availabilityRepository.findByEmployee_Id(employeeId);
    }
}