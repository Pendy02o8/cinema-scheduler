package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Availability;
import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import com.pendy.cinema_scheduler.repository.AvailabilityRepository;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pendy.cinema_scheduler.entity.PositionRequirement;
import com.pendy.cinema_scheduler.repository.PositionRequirementRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleAssignmentService {

    private final ScheduleAssignmentRepository scheduleAssignmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final PositionRequirementRepository positionRequirementRepository;

    public List<ScheduleAssignment> getAllScheduleAssignments() {
        return scheduleAssignmentRepository.findAll();
    }

    public ScheduleAssignment getScheduleAssignmentById(Long id) {
        return scheduleAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到排班資料"));
    }

    public ScheduleAssignment createScheduleAssignment(ScheduleAssignment scheduleAssignment) {
        //檢查排班衝突(同一位員工有無重複排班)
        Long employeeId = scheduleAssignment.getEmployee().getId();

        List<ScheduleAssignment> conflicts = scheduleAssignmentRepository.findByEmployee_IdAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                employeeId,scheduleAssignment.getDate(),scheduleAssignment.getEndTime(),scheduleAssignment.getStartTime());

        if(!conflicts.isEmpty()){
            throw new RuntimeException("此員工在該時段已有排班，不能重複排班");
        }

        //檢查排班衝突(該員工該時段是否可排班)
        List<Availability> availabilities = availabilityRepository.findByEmployee_IdAndDate(employeeId,scheduleAssignment.getDate());

        Availability availability = availabilities.get(0);
        if("OFF".equals(availability.getAvailabilityType())){
            throw new RuntimeException("此員工當天休假，不能排班");
        }
        if("AFTER".equals(availability.getAvailabilityType())){
            if (scheduleAssignment.getStartTime().isBefore(availability.getBoundaryTime())){
                throw new RuntimeException("此員工只能在"+availability.getBoundaryTime()+"之後上班");
            }
        }

        if("BEFORE".equals(availability.getAvailabilityType())){
            if (scheduleAssignment.getEndTime().isAfter(availability.getBoundaryTime())){
                throw new RuntimeException("此員工只能在"+availability.getBoundaryTime()+"之前上班");
            }
        }
        return scheduleAssignmentRepository.save(scheduleAssignment);
    }

    public List<String> checkGaps(LocalDate date) {

        List<String> result = new ArrayList<>();

        List<PositionRequirement> requirements =
                positionRequirementRepository.findByDate(date);

        for (PositionRequirement requirement : requirements) {

            Long positionId = requirement.getPosition().getId();

            List<ScheduleAssignment> assignments =
                    scheduleAssignmentRepository.findByDateAndPosition_Id(
                            date,
                            positionId
                    );

            LocalTime gapStart = null;
            LocalTime current = requirement.getStartTime();

            while (current.isBefore(requirement.getEndTime())) {

                LocalTime next = current.plusMinutes(10);

                LocalTime finalCurrent = current;
                long assignedCount = assignments.stream()
                        .filter(assignment ->
                                assignment.getStartTime().isBefore(next)
                                        && assignment.getEndTime().isAfter(finalCurrent)
                        )
                        .count();

                boolean isGap = assignedCount < requirement.getRequiredCount();

                if (isGap && gapStart == null) {
                    gapStart = current;
                }

                if (!isGap && gapStart != null) {
                    result.add(
                            requirement.getPosition().getName()
                                    + " "
                                    + gapStart
                                    + "~"
                                    + current
                                    + " 缺人 "
                    );
                    gapStart = null;
                }

                current = next;
            }

            if (gapStart != null) {
                result.add(
                        requirement.getPosition().getName()
                                + " "
                                + gapStart
                                + "~"
                                + requirement.getEndTime()
                                + " 缺人"
                );
            }
        }

        return result;
    }

    public ScheduleAssignment updateScheduleAssignment(Long id, ScheduleAssignment newScheduleAssignment) {
        ScheduleAssignment scheduleAssignment = getScheduleAssignmentById(id);

        scheduleAssignment.setWeeklySchedule(newScheduleAssignment.getWeeklySchedule());
        scheduleAssignment.setEmployee(newScheduleAssignment.getEmployee());
        scheduleAssignment.setPosition(newScheduleAssignment.getPosition());
        scheduleAssignment.setDate(newScheduleAssignment.getDate());
        scheduleAssignment.setStartTime(newScheduleAssignment.getStartTime());
        scheduleAssignment.setEndTime(newScheduleAssignment.getEndTime());
        scheduleAssignment.setNote(newScheduleAssignment.getNote());

        return scheduleAssignmentRepository.save(scheduleAssignment);
    }

    public void deleteScheduleAssignment(Long id) {
        scheduleAssignmentRepository.deleteById(id);
    }

    public List<ScheduleAssignment> getScheduleAssignmentsByDate(LocalDate date) {
        return scheduleAssignmentRepository.findByDate(date);
    }

    public List<ScheduleAssignment> getScheduleAssignmentsByEmployeeId(Long employeeId) {
        return scheduleAssignmentRepository.findByEmployeeId(employeeId);
    }

    public List<ScheduleAssignment> getScheduleAssignmentsByPositionId(Long positionId) {
        return scheduleAssignmentRepository.findByPositionId(positionId);
    }
}