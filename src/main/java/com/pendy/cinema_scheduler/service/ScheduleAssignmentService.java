package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.Availability;
import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import com.pendy.cinema_scheduler.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pendy.cinema_scheduler.entity.PositionRequirement;
import com.pendy.cinema_scheduler.repository.WeeklyScheduleRepository;
import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import com.pendy.cinema_scheduler.dto.ScheduleAssignmentResponse;
import com.pendy.cinema_scheduler.dto.ScheduleValidationResponse;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleAssignmentService {

    private final ScheduleAssignmentRepository scheduleAssignmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final PositionRequirementRepository positionRequirementRepository;
    private final MonthlyLeaveService monthlyLeaveService;
    private final EmployeeRepository employeeRepository;
    private final WeeklyScheduleRepository weeklyScheduleRepository;

    public List<ScheduleAssignment> getAllScheduleAssignments() {
        return scheduleAssignmentRepository.findAll();
    }

    public ScheduleAssignment getScheduleAssignmentById(Long id) {
        return scheduleAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到排班資料"));
    }

    public ScheduleAssignmentResponse createScheduleAssignment(ScheduleAssignment scheduleAssignment) {

        Long employeeId = scheduleAssignment.getEmployee().getId();

        // 1. 檢查同一位員工同一天同時段是否重複排班
        List<ScheduleAssignment> conflicts =
                scheduleAssignmentRepository
                        .findByEmployee_IdAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                                employeeId,
                                scheduleAssignment.getDate(),
                                scheduleAssignment.getEndTime(),
                                scheduleAssignment.getStartTime()
                        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("此員工在該時段已有排班，不能重複排班");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("找不到員工 id: " + employeeId));

        String employeeType = employee.getEmployeeType();

        scheduleAssignment.setEmployee(employee);

        // 2. 工讀生：照availability
        String warningMessage = null;
        if ("PART_TIME".equals(employeeType)) {

            List<Availability> availabilities =
                    availabilityRepository.findByEmployee_IdAndDate(
                            employeeId,
                            scheduleAssignment.getDate()
                    );

            if (availabilities.isEmpty()) {
                throw new RuntimeException("此工讀生當天沒有填寫可上班時間，不能排班");
            }

            Availability availability = availabilities.get(0);

            if ("OFF".equals(availability.getAvailabilityType())) {
                throw new RuntimeException("此員工當天休假，不能排班");
            }


        }

        // 3. 正職 / 清潔：不看 availability，只看 monthly_leaves
        else if ("FULL_TIME".equals(employeeType) || "CLEANER".equals(employeeType)) {

            boolean isOnLeave = monthlyLeaveService.isEmployeeOnLeave(
                    employeeId,
                    scheduleAssignment.getDate()
            );

            if (isOnLeave) {
                throw new RuntimeException("此員工當天排休，不能排班");
            }
        }

        // 4. 防呆：員工類型沒設定
        else {
            throw new RuntimeException("此員工尚未設定 employeeType，不能排班");
        }

        String jobTitle = employee.getJobTitle();

        boolean noPositionRequired =
                "副理".equals(jobTitle)
                        || "會計".equals(jobTitle)
                        || "正職清潔".equals(jobTitle)
                        || "晚班清潔".equals(jobTitle);

        if (noPositionRequired) {
            scheduleAssignment.setPosition(null);
        }

        if (!noPositionRequired && scheduleAssignment.getPosition() == null) {
            throw new RuntimeException("此職稱需要選擇崗位");
        }
        ScheduleAssignment saved = scheduleAssignmentRepository.save(scheduleAssignment);

        return new ScheduleAssignmentResponse(saved, Collections.singletonList(warningMessage));
    }
    //檢查排班衝突警告
    public ScheduleValidationResponse validateScheduleAssignment(
            ScheduleAssignment scheduleAssignment
    ) {
        List<String> warnings = new ArrayList<>();

        Long employeeId = scheduleAssignment.getEmployee().getId();

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("找不到員工 id: " + employeeId));

        if (!"PART_TIME".equals(employee.getEmployeeType())) {
            return new ScheduleValidationResponse(warnings);
        }

        List<Availability> availabilities =
                availabilityRepository.findByEmployee_IdAndDate(
                        employeeId,
                        scheduleAssignment.getDate()
                );

        if (availabilities.isEmpty()) {
            throw new RuntimeException("此工讀生當天沒有填寫可上班時間，不能排班");
        }

        Availability availability = availabilities.get(0);

        if ("OFF".equals(availability.getAvailabilityType())) {
            throw new RuntimeException("此員工當天休假，不能排班");
        }

        if ("AFTER".equals(availability.getAvailabilityType())) {
            if (scheduleAssignment.getStartTime().isBefore(availability.getBoundaryTime())) {
                warnings.add(
                        "此員工原本只能在 "
                                + availability.getBoundaryTime()
                                + " 之後上班，但本次排班早於可上班時間"
                );
            }
        }

        if ("BEFORE".equals(availability.getAvailabilityType())) {
            if (scheduleAssignment.getEndTime().isAfter(availability.getBoundaryTime())) {
                warnings.add(
                        "此員工原本只能在 "
                                + availability.getBoundaryTime()
                                + " 之前上班，但本次排班晚於可上班時間"
                );
            }
        }

        return new ScheduleValidationResponse(warnings);
    }

    //產生正職固定班
    public List<ScheduleAssignment> generateFixedSchedule(
            Long weeklyScheduleId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        WeeklySchedule weeklySchedule = weeklyScheduleRepository.findById(weeklyScheduleId)
                .orElseThrow(() -> new RuntimeException("找不到週班表 id: " + weeklyScheduleId));

        List<Employee> fixedEmployees = employeeRepository.findByEmployeeTypeIn(
                List.of("FULL_TIME", "CLEANER")
        );

        List<ScheduleAssignment> createdAssignments = new ArrayList<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {

            for (Employee employee : fixedEmployees) {

                boolean isOnLeave = monthlyLeaveService.isEmployeeOnLeave(
                        employee.getId(),
                        currentDate
                );

                if (isOnLeave) {
                    // 月休不用建立排班，前端依 monthly_leaves 顯示「休」
                    continue;
                }

                ScheduleAssignment assignment = new ScheduleAssignment();

                assignment.setWeeklySchedule(weeklySchedule);
                assignment.setEmployee(employee);
                assignment.setDate(currentDate);
                assignment.setPosition(null);

                if ("CLEANER".equals(employee.getEmployeeType())) {

                    if ("正職清潔".equals(employee.getJobTitle())) {
                        assignment.setStartTime(LocalTime.of(12, 50));
                        assignment.setEndTime(LocalTime.of(22, 0));
                    } else {
                        continue;
                    }

                } else if ("FULL_TIME".equals(employee.getEmployeeType())) {

                    if ("MORNING".equals(employee.getFixedShiftType())) {
                        assignment.setStartTime(LocalTime.of(8, 50));
                        assignment.setEndTime(LocalTime.of(17, 30));
                    } else if ("EVENING".equals(employee.getFixedShiftType())) {
                        assignment.setStartTime(LocalTime.of(16, 50));
                        assignment.setEndTime(LocalTime.of(1, 30));
                    } else {
                        continue;
                    }
                }


                List<ScheduleAssignment> sameDayAssignments =
                        scheduleAssignmentRepository.findByEmployee_IdAndDate(
                                employee.getId(),
                                currentDate
                        );

                if (!sameDayAssignments.isEmpty()) {
                    continue;
                }

                ScheduleAssignment saved =
                        scheduleAssignmentRepository.save(assignment);

                createdAssignments.add(saved);
            }

            currentDate = currentDate.plusDays(1);
        }

        return createdAssignments;
    }
    private int toMinutes(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }

    private boolean overlapsTimeSlot(
            LocalTime assignmentStart,
            LocalTime assignmentEnd,
            LocalTime slotStart,
            LocalTime slotEnd
    ) {
        int assignmentStartMin = toMinutes(assignmentStart);
        int assignmentEndMin = toMinutes(assignmentEnd);

        int slotStartMin = toMinutes(slotStart);
        int slotEndMin = toMinutes(slotEnd);

        // 排班跨日，例如 16:50~01:30
        if (assignmentEndMin <= assignmentStartMin) {
            assignmentEndMin += 24 * 60;
        }

        // 檢查區間如果跨日，也補上
        if (slotEndMin <= slotStartMin) {
            slotEndMin += 24 * 60;
        }

        return assignmentStartMin < slotEndMin
                && assignmentEndMin > slotStartMin;
    }
    //檢查所有崗位在何時段是否缺人
    public List<String> checkGaps(LocalDate date) {

        List<String> result = new ArrayList<>();

        List<PositionRequirement> requirements =
                positionRequirementRepository.findAll();

        for (PositionRequirement requirement : requirements) {
            if (Boolean.FALSE.equals(requirement.getPosition().getIsRequired())) {
                continue;
            }

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
                                overlapsTimeSlot(
                                        assignment.getStartTime(),
                                        assignment.getEndTime(),
                                        finalCurrent,
                                        next
                                )
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
                                overlapsTimeSlot(
                                        assignment.getStartTime(),
                                        assignment.getEndTime(),
                                        finalCurrent,
                                        next
                                )
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

                    long overMinutes = Duration.between(overStart, current).toMinutes();

                    if (overMinutes > 60) {
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
                    }

                    overStart = null;
                    maxOverAssignedCount = 0;
                }

                current = next;
            }

            if (overStart != null) {

                long overMinutes = Duration.between(
                        overStart,
                        requirement.getEndTime()
                ).toMinutes();

                if (overMinutes > 60) {
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
    //查某週班表是否缺人/超編
    public List<String> checkScheduleByWeek(
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<String> result = new ArrayList<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {

            List<String> dailyResult = checkSchedule(currentDate);

            for (String message : dailyResult) {
                if (!message.equals("此日班表無缺人或超編問題")) {
                    result.add(currentDate + " " + message);
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        if (result.isEmpty()) {
            result.add("此週班表無缺人或超編問題");
        }

        return result;
    }
    //查某週班表
    public List<ScheduleAssignment> getScheduleAssignmentsByWeek(
            LocalDate startDate,
            LocalDate endDate
    ) {
        return scheduleAssignmentRepository.findByDateBetween(
                startDate,
                endDate
        );
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