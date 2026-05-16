package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleAssignmentService {

    private final ScheduleAssignmentRepository scheduleAssignmentRepository;

    public List<ScheduleAssignment> getAllScheduleAssignments() {
        return scheduleAssignmentRepository.findAll();
    }
}