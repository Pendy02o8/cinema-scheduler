package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.*;
import com.pendy.cinema_scheduler.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pendy.cinema_scheduler.repository.WeeklyScheduleRepository;
import com.pendy.cinema_scheduler.dto.ScheduleAssignmentResponse;
import com.pendy.cinema_scheduler.dto.ScheduleValidationResponse;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleAssignmentService {

    private static final String REST_POSITION_NAME = "休";

    private final ScheduleAssignmentRepository scheduleAssignmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final PositionRequirementRepository positionRequirementRepository;
    private final MonthlyLeaveService monthlyLeaveService;
    private final EmployeeRepository employeeRepository;
    private final WeeklyScheduleRepository weeklyScheduleRepository;
    private final PositionRepository positionRepository;
    private final ScheduleAssignmentChangeRepository scheduleAssignmentChangeRepository;

    public List<ScheduleAssignment> getAllScheduleAssignments() {
        return scheduleAssignmentRepository.findAll();
    }

    public ScheduleAssignment getScheduleAssignmentById(Long id) {
        return scheduleAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到排班資料"));
    }

    public ScheduleAssignmentResponse createScheduleAssignment(ScheduleAssignment scheduleAssignment) {

        Long employeeId = scheduleAssignment.getEmployee().getId();

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("找不到員工 id: " + employeeId));

        String employeeType = employee.getEmployeeType();

        scheduleAssignment.setEmployee(employee);
        boolean restAssignment = isRestAssignment(scheduleAssignment);

        ensureRestDayDoesNotMixWithWork(scheduleAssignment, employeeId, null, restAssignment);

        // 1. 檢查同一位員工同一天同時段是否重複排班
        if (!restAssignment) {
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
        }

        List<String> warnings = new ArrayList<>();

        // 2. 工讀生：照availability
        if (restAssignment) {
            // 「休」是特殊排班狀態，不受 availability / monthly_leaves 限制。
        } else if ("PART_TIME".equals(employeeType)) {
            addAvailabilityWarning(warnings, scheduleAssignment, employeeId);
        }

        // 3. 正職 / 清潔：不看 availability，只看 monthly_leaves
        else if ("FULL_TIME".equals(employeeType) || "CLEANER".equals(employeeType)) {

            boolean isOnLeave = monthlyLeaveService.isEmployeeOnLeave(
                    employeeId,
                    scheduleAssignment.getDate()
            );

            if (isOnLeave) {
                warnings.add("此員工當天排休，請確認是否仍要排班");
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

        if (noPositionRequired && !restAssignment) {
            scheduleAssignment.setPosition(null);
        }

        if (!noPositionRequired && scheduleAssignment.getPosition() == null) {
            throw new RuntimeException("此職稱需要選擇崗位");
        }

        ScheduleAssignment saved = scheduleAssignmentRepository.save(scheduleAssignment);
        recordChangeIfPublished(saved, "CREATED");

        return new ScheduleAssignmentResponse(saved, warnings);
    }
    //檢查排班衝突警告
    public ScheduleValidationResponse validateScheduleAssignment(
            ScheduleAssignment scheduleAssignment
    ) {
        List<String> warnings = new ArrayList<>();

        Long employeeId = scheduleAssignment.getEmployee().getId();

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("找不到員工 id: " + employeeId));

        if (isRestAssignment(scheduleAssignment)) {
            return new ScheduleValidationResponse(warnings);
        }

        if ("PART_TIME".equals(employee.getEmployeeType())) {
            addAvailabilityWarning(warnings, scheduleAssignment, employeeId);
        } else if ("FULL_TIME".equals(employee.getEmployeeType()) || "CLEANER".equals(employee.getEmployeeType())) {
            boolean isOnLeave = monthlyLeaveService.isEmployeeOnLeave(
                    employeeId,
                    scheduleAssignment.getDate()
            );

            if (isOnLeave) {
                warnings.add("此員工當天排休，請確認是否仍要排班");
            }
        }

        return new ScheduleValidationResponse(warnings);
    }

    private void addAvailabilityWarning(
            List<String> warnings,
            ScheduleAssignment scheduleAssignment,
            Long employeeId
    ) {
        boolean available = isEmployeeAvailable(
                employeeId,
                scheduleAssignment.getDate(),
                scheduleAssignment.getStartTime(),
                scheduleAssignment.getEndTime()
        );

        if (!available) {
            warnings.add("該員工這個時段無法上班");
        }
    }

    private boolean isEmployeeAvailable(
            Long employeeId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        if (availabilityRepository.count() == 0) {
            return true;
        }

        if (!availabilityRepository.existsByEmployee_Id(employeeId)) {
            return true;
        }

        List<Availability> availabilities =
                availabilityRepository.findByEmployee_IdAndDate(employeeId, date);

        if (availabilities.isEmpty()) {
            return false;
        }

        Availability availability = availabilities.get(0);
        String availabilityType = availability.getAvailabilityType();

        if ("ALL_DAY".equals(availabilityType)) {
            return true;
        }

        if ("OFF".equals(availabilityType)) {
            return false;
        }

        if ("AFTER".equals(availabilityType)) {
            return availability.getBoundaryTime() != null
                    && !startTime.isBefore(availability.getBoundaryTime());
        }

        if ("BEFORE".equals(availabilityType)) {
            return availability.getBoundaryTime() != null
                    && !endTime.isAfter(availability.getBoundaryTime());
        }

        return false;
    }

    private void ensureRestDayDoesNotMixWithWork(
            ScheduleAssignment scheduleAssignment,
            Long employeeId,
            Long currentAssignmentId,
            boolean restAssignment
    ) {
        List<ScheduleAssignment> sameDayAssignments =
                scheduleAssignmentRepository.findByEmployee_IdAndDate(
                        employeeId,
                        scheduleAssignment.getDate()
                );

        List<ScheduleAssignment> otherAssignments = sameDayAssignments.stream()
                .filter(existing -> currentAssignmentId == null
                        || existing.getId() == null
                        || !existing.getId().equals(currentAssignmentId))
                .toList();

        if (restAssignment && !otherAssignments.isEmpty()) {
            throw new RuntimeException("此員工當天已有排班，不能排休");
        }

        boolean hasRestAssignment = otherAssignments.stream()
                .anyMatch(this::isRestAssignment);

        if (!restAssignment && hasRestAssignment) {
            throw new RuntimeException("此員工當天已排休，不能再排上班班別");
        }
    }

    private boolean isRestAssignment(ScheduleAssignment scheduleAssignment) {
        if (scheduleAssignment == null || scheduleAssignment.getPosition() == null) {
            return false;
        }

        Position position = scheduleAssignment.getPosition();

        if (REST_POSITION_NAME.equals(position.getName())) {
            return true;
        }

        if (position.getId() == null) {
            return false;
        }

        return positionRepository.findById(position.getId())
                .map(foundPosition -> REST_POSITION_NAME.equals(foundPosition.getName()))
                .orElse(false);
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

                recordChangeIfPublished(saved, "CREATED");

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
            if (shouldSkipRequirement(requirement)) {
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
            if (shouldSkipRequirement(requirement)) {
                continue;
            }

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

    private boolean shouldSkipRequirement(PositionRequirement requirement) {
        return Boolean.FALSE.equals(requirement.getPosition().getIsRequired())
                || REST_POSITION_NAME.equals(requirement.getPosition().getName());
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
    //班表異動記錄
    private void recordChangeIfPublished(ScheduleAssignment assignment, String changeType) {
        if (assignment.getWeeklySchedule() == null || assignment.getWeeklySchedule().getId() == null) {
            return;
        }

        Long weeklyScheduleId = assignment.getWeeklySchedule().getId();

        WeeklySchedule weeklySchedule = weeklyScheduleRepository.findById(weeklyScheduleId)
                .orElseThrow(() -> new RuntimeException("找不到週班表 id: " + weeklyScheduleId));

        if (!"PUBLISHED".equals(weeklySchedule.getStatus())) {
            return;
        }

        if (assignment.getEmployee() == null || assignment.getEmployee().getId() == null) {
            return;
        }

        boolean exists = scheduleAssignmentChangeRepository
                .existsByWeeklySchedule_IdAndEmployee_IdAndDate(
                        weeklySchedule.getId(),
                        assignment.getEmployee().getId(),
                        assignment.getDate()
                );

        if (exists) {
            return;
        }

        ScheduleAssignmentChange change = new ScheduleAssignmentChange();
        change.setWeeklySchedule(weeklySchedule);
        change.setEmployee(assignment.getEmployee());
        change.setDate(assignment.getDate());
        change.setChangeType(changeType);
        change.setCreatedAt(LocalDateTime.now());

        scheduleAssignmentChangeRepository.save(change);
    }
    private double calculatePaidHours(ScheduleAssignment assignment) {
        if (isRestAssignment(assignment)) {
            return 0;
        }

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
    public ScheduleAssignmentResponse updateScheduleAssignment(Long id, ScheduleAssignment newScheduleAssignment) {
        ScheduleAssignment scheduleAssignment = getScheduleAssignmentById(id);
        Long employeeId = newScheduleAssignment.getEmployee().getId();

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("找不到員工 id: " + employeeId));

        String employeeType = employee.getEmployeeType();
        boolean restAssignment = isRestAssignment(newScheduleAssignment);

        ensureRestDayDoesNotMixWithWork(newScheduleAssignment, employeeId, id, restAssignment);

        if (!restAssignment) {
            List<ScheduleAssignment> conflicts =
                    scheduleAssignmentRepository
                            .findByEmployee_IdAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                                    employeeId,
                                    newScheduleAssignment.getDate(),
                                    newScheduleAssignment.getEndTime(),
                                    newScheduleAssignment.getStartTime()
                            )
                            .stream()
                            .filter(existing -> existing.getId() == null || !existing.getId().equals(id))
                            .toList();

            if (!conflicts.isEmpty()) {
                throw new RuntimeException("此員工在該時段已有排班，不能重複排班");
            }
        }

        List<String> warnings = new ArrayList<>();

        if (!restAssignment && "PART_TIME".equals(employeeType)) {
            addAvailabilityWarning(warnings, newScheduleAssignment, employeeId);
        } else if (!restAssignment && ("FULL_TIME".equals(employeeType) || "CLEANER".equals(employeeType))) {
            boolean isOnLeave = monthlyLeaveService.isEmployeeOnLeave(
                    employeeId,
                    newScheduleAssignment.getDate()
            );

            if (isOnLeave) {
                warnings.add("此員工當天排休，請確認是否仍要排班");
            }
        } else if (!restAssignment) {
            throw new RuntimeException("此員工尚未設定 employeeType，不能排班");
        }

        String jobTitle = employee.getJobTitle();

        boolean noPositionRequired =
                "副理".equals(jobTitle)
                        || "會計".equals(jobTitle)
                        || "正職清潔".equals(jobTitle)
                        || "晚班清潔".equals(jobTitle);

        if (noPositionRequired && !restAssignment) {
            newScheduleAssignment.setPosition(null);
        }

        if (!noPositionRequired && newScheduleAssignment.getPosition() == null) {
            throw new RuntimeException("此職稱需要選擇崗位");
        }

        scheduleAssignment.setWeeklySchedule(newScheduleAssignment.getWeeklySchedule());
        scheduleAssignment.setEmployee(employee);
        scheduleAssignment.setPosition(newScheduleAssignment.getPosition());
        scheduleAssignment.setDate(newScheduleAssignment.getDate());
        scheduleAssignment.setStartTime(newScheduleAssignment.getStartTime());
        scheduleAssignment.setEndTime(newScheduleAssignment.getEndTime());
        scheduleAssignment.setNote(newScheduleAssignment.getNote());

        ScheduleAssignment saved = scheduleAssignmentRepository.save(scheduleAssignment);

        recordChangeIfPublished(saved, "UPDATED");

        return new ScheduleAssignmentResponse(saved, warnings);
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
        ScheduleAssignment scheduleAssignment = scheduleAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到班表"));

        recordChangeIfPublished(scheduleAssignment, "DELETED");

        scheduleAssignmentRepository.delete(scheduleAssignment);
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
