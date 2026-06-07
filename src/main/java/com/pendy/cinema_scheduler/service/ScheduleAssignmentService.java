package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Availability;
import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import com.pendy.cinema_scheduler.repository.AvailabilityRepository;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pendy.cinema_scheduler.entity.PositionRequirement;
import com.pendy.cinema_scheduler.repository.PositionRequirementRepository;

import java.time.Duration;
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
    //檢查所有崗位在何時段是否缺人
    public List<String> checkGaps(LocalDate date) {

        List<String> result = new ArrayList<>();

        List<PositionRequirement> requirements =
                positionRequirementRepository.findAll();

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
    //檢查已排崗位是否有超編
    public List<String> checkOverstaffed(LocalDate date) {

        List<String> result = new ArrayList<>();

        List<PositionRequirement> requirements =
                positionRequirementRepository.findAll();

        for (PositionRequirement requirement : requirements) {

            Long positionId = requirement.getPosition().getId();

            List<ScheduleAssignment> assignments =
                    scheduleAssignmentRepository.findByDateAndPosition_Id(
                            date,
                            positionId
                    );

            LocalTime overStart = null;
            LocalTime current = requirement.getStartTime();
            long maxOverAssignedCount = 0;

            while (current.isBefore(requirement.getEndTime())) {

                LocalTime next = current.plusMinutes(10);

                LocalTime finalCurrent = current;
                long assignedCount = assignments.stream()
                        .filter(assignment ->
                                assignment.getStartTime().isBefore(next)
                                        && assignment.getEndTime().isAfter(finalCurrent)
                        )
                        .count();

                boolean isOverstaffed =
                        assignedCount > requirement.getRequiredCount();

                if (isOverstaffed) {
                    maxOverAssignedCount = Math.max(maxOverAssignedCount, assignedCount);
                }

                if (isOverstaffed && overStart == null) {
                    overStart = current;
                }

                if (!isOverstaffed && overStart != null) {
                    result.add(
                            requirement.getPosition().getName()
                                    + " "
                                    + overStart
                                    + "~"
                                    + current
                                    + " 超編 "
                                    + (maxOverAssignedCount - requirement.getRequiredCount())
                                    + " 人"
                    );

                    overStart = null;
                    maxOverAssignedCount = 0;
                }

                current = next;
            }

            if (overStart != null) {
                result.add(
                        requirement.getPosition().getName()
                                + " "
                                + overStart
                                + "~"
                                + requirement.getEndTime()
                                + " 超編 "
                                + (maxOverAssignedCount - requirement.getRequiredCount())
                                + " 人"
                );
            }
        }

        return result;
    }
    // 工時計算
    public String getEmployeeWorkHours(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<ScheduleAssignment> assignments =
                scheduleAssignmentRepository.findByEmployee_IdAndDateBetween(
                        employeeId,
                        startDate,
                        endDate
                );

        if (assignments.isEmpty()) {
            return "此員工在指定日期區間沒有排班";
        }

        double totalHours = 0;
        String employeeName = "";

        for (ScheduleAssignment assignment : assignments) {
            totalHours += calculatePaidHours(assignment);
            employeeName = assignment.getEmployee().getName();
        }

        return employeeName
                + " "
                + startDate
                + "~"
                + endDate
                + " 工時："
                + formatHours(totalHours);
    }

    private double calculatePaidHours(ScheduleAssignment assignment) {
        long minutes = Duration.between(
                assignment.getStartTime(),
                assignment.getEndTime()
        ).toMinutes();

        // 上班滿4小時，扣30分鐘休息
        if (minutes >= 240) {
            minutes -= 30;
        }

        // 以半小時為單位，無條件捨去
        long halfHourUnits = minutes / 30;

        return halfHourUnits * 0.5;
    }

    private String formatHours(double hours) {
        if (hours == (long) hours) {
            return String.format("%d小時", (long) hours);
        }

        return String.format("%.1f小時", hours);
    }
    //查全部員工工時
    public List<String> getAllEmployeesWorkHours(
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<ScheduleAssignment> assignments =
                scheduleAssignmentRepository.findByDateBetween(
                        startDate,
                        endDate
                );

        List<String> result = new ArrayList<>();

        List<Long> employeeIds = new ArrayList<>();

        for (ScheduleAssignment assignment : assignments) {
            Long employeeId = assignment.getEmployee().getId();

            if (!employeeIds.contains(employeeId)) {
                employeeIds.add(employeeId);
            }
        }

        for (Long employeeId : employeeIds) {
            double totalHours = 0;
            String employeeName = "";

            for (ScheduleAssignment assignment : assignments) {
                if (assignment.getEmployee().getId().equals(employeeId)) {
                    totalHours += calculatePaidHours(assignment);
                    employeeName = assignment.getEmployee().getName();
                }
            }

            result.add(employeeName + "：" + formatHours(totalHours));
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
    //列出某日是否有缺人or超編
    public List<String> checkSchedule(LocalDate date) {

        List<String> result = new ArrayList<>();

        List<String> gaps = checkGaps(date);
        List<String> overstaffed = checkOverstaffed(date);

        for (String gap : gaps) {
            result.add("缺人：" + gap);
        }

        for (String over : overstaffed) {
            result.add("超編：" + over);
        }

        if (result.isEmpty()) {
            result.add("此日班表無缺人或超編問題");
        }

        return result;
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