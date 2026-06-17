package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.dto.ScheduleAssignmentResponse;
import com.pendy.cinema_scheduler.dto.ScheduleValidationResponse;
import com.pendy.cinema_scheduler.entity.Availability;
import com.pendy.cinema_scheduler.entity.Employee;
import com.pendy.cinema_scheduler.entity.Position;
import com.pendy.cinema_scheduler.entity.PositionRequirement;
import com.pendy.cinema_scheduler.entity.ScheduleAssignment;
import com.pendy.cinema_scheduler.entity.ScheduleAssignmentChange;
import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import com.pendy.cinema_scheduler.repository.AvailabilityRepository;
import com.pendy.cinema_scheduler.repository.EmployeeRepository;
import com.pendy.cinema_scheduler.repository.PositionRepository;
import com.pendy.cinema_scheduler.repository.PositionRequirementRepository;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentChangeRepository;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentRepository;
import com.pendy.cinema_scheduler.repository.WeeklyScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleAssignmentServiceTest {

    private static final Long EMPLOYEE_ID = 1L;
    private static final LocalDate ASSIGNMENT_DATE = LocalDate.of(2026, 6, 15);

    @Mock
    private ScheduleAssignmentRepository scheduleAssignmentRepository;

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private PositionRequirementRepository positionRequirementRepository;

    @Mock
    private MonthlyLeaveService monthlyLeaveService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private WeeklyScheduleRepository weeklyScheduleRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private ScheduleAssignmentChangeRepository scheduleAssignmentChangeRepository;

    @InjectMocks
    private ScheduleAssignmentService scheduleAssignmentService;

    private Employee employee;
    private ScheduleAssignment assignment;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(EMPLOYEE_ID);
        employee.setName("Part Timer");
        employee.setJobTitle("售票");
        employee.setEmployeeType("PART_TIME");

        Position position = new Position();
        position.setId(1L);
        position.setName("售票");

        assignment = new ScheduleAssignment();
        assignment.setEmployee(employee);
        assignment.setPosition(position);
        assignment.setDate(ASSIGNMENT_DATE);
        assignment.setStartTime(LocalTime.of(10, 0));
        assignment.setEndTime(LocalTime.of(14, 0));
    }

    @Test
    void createScheduleAssignmentAllowsPartTimerWhenAvailabilityTableIsEmpty() {
        stubEmployeeLookup();
        stubNoSameDayAssignments();
        stubNoTimeOverlap();
        when(availabilityRepository.count()).thenReturn(0L);
        stubSave();

        ScheduleAssignmentResponse response = assertDoesNotThrow(
                () -> scheduleAssignmentService.createScheduleAssignment(assignment)
        );

        verify(scheduleAssignmentRepository).save(assignment);
        assertNotNull(response.getData());
    }

    @Test
    void createScheduleAssignmentAllowsPartTimerWithoutEmployeeAvailability() {
        stubEmployeeLookup();
        stubNoSameDayAssignments();
        stubNoTimeOverlap();
        when(availabilityRepository.count()).thenReturn(1L);
        when(availabilityRepository.existsByEmployee_Id(EMPLOYEE_ID)).thenReturn(false);
        stubSave();

        assertDoesNotThrow(() -> scheduleAssignmentService.createScheduleAssignment(assignment));

        verify(scheduleAssignmentRepository).save(assignment);
    }

    @Test
    void createScheduleAssignmentAllowsPartTimerWhenAvailabilityTimeMatches() {
        stubEmployeeLookup();
        stubNoSameDayAssignments();
        stubNoTimeOverlap();
        when(availabilityRepository.count()).thenReturn(1L);
        when(availabilityRepository.existsByEmployee_Id(EMPLOYEE_ID)).thenReturn(true);
        when(availabilityRepository.findByEmployee_IdAndDate(EMPLOYEE_ID, ASSIGNMENT_DATE))
                .thenReturn(List.of(availability("AFTER", LocalTime.of(9, 0))));
        stubSave();

        assertDoesNotThrow(() -> scheduleAssignmentService.createScheduleAssignment(assignment));

        verify(scheduleAssignmentRepository).save(assignment);
    }

    @Test
    void createScheduleAssignmentWarnsPartTimerWhenAvailabilityTimeDoesNotMatch() {
        stubEmployeeLookup();
        stubNoSameDayAssignments();
        stubNoTimeOverlap();
        when(availabilityRepository.count()).thenReturn(1L);
        when(availabilityRepository.existsByEmployee_Id(EMPLOYEE_ID)).thenReturn(true);
        when(availabilityRepository.findByEmployee_IdAndDate(EMPLOYEE_ID, ASSIGNMENT_DATE))
                .thenReturn(List.of(availability("AFTER", LocalTime.of(12, 0))));
        stubSave();

        ScheduleAssignmentResponse response = assertDoesNotThrow(
                () -> scheduleAssignmentService.createScheduleAssignment(assignment)
        );

        verify(scheduleAssignmentRepository).save(assignment);
        assertTrue(response.getWarnings().contains("該員工這個時段無法上班"));
    }

    @Test
    void createScheduleAssignmentWarnsPartTimerWhenAvailabilityDateDoesNotMatch() {
        stubEmployeeLookup();
        stubNoSameDayAssignments();
        stubNoTimeOverlap();
        when(availabilityRepository.count()).thenReturn(1L);
        when(availabilityRepository.existsByEmployee_Id(EMPLOYEE_ID)).thenReturn(true);
        when(availabilityRepository.findByEmployee_IdAndDate(EMPLOYEE_ID, ASSIGNMENT_DATE))
                .thenReturn(Collections.emptyList());
        stubSave();

        ScheduleAssignmentResponse response = assertDoesNotThrow(
                () -> scheduleAssignmentService.createScheduleAssignment(assignment)
        );

        verify(scheduleAssignmentRepository).save(assignment);
        assertTrue(response.getWarnings().contains("該員工這個時段無法上班"));
    }

    @Test
    void createScheduleAssignmentDoesNotRecordChangeWhenWeeklyScheduleIsDraft() {
        WeeklySchedule weeklySchedule = weeklySchedule(9L, "DRAFT");
        assignment.setWeeklySchedule(weeklySchedule);
        stubEmployeeLookup();
        stubNoSameDayAssignments();
        stubNoTimeOverlap();
        when(availabilityRepository.count()).thenReturn(0L);
        when(weeklyScheduleRepository.findById(9L)).thenReturn(Optional.of(weeklySchedule));
        stubSave();

        assertDoesNotThrow(() -> scheduleAssignmentService.createScheduleAssignment(assignment));

        verify(scheduleAssignmentChangeRepository, never()).save(any(ScheduleAssignmentChange.class));
    }

    @Test
    void createScheduleAssignmentRecordsChangeWhenWeeklyScheduleIsPublished() {
        WeeklySchedule weeklySchedule = weeklySchedule(9L, "PUBLISHED");
        assignment.setWeeklySchedule(weeklySchedule);
        stubEmployeeLookup();
        stubNoSameDayAssignments();
        stubNoTimeOverlap();
        when(availabilityRepository.count()).thenReturn(0L);
        when(weeklyScheduleRepository.findById(9L)).thenReturn(Optional.of(weeklySchedule));
        when(scheduleAssignmentChangeRepository.existsByWeeklySchedule_IdAndEmployee_IdAndDate(
                9L,
                EMPLOYEE_ID,
                ASSIGNMENT_DATE
        )).thenReturn(false);
        stubSave();

        assertDoesNotThrow(() -> scheduleAssignmentService.createScheduleAssignment(assignment));

        verify(scheduleAssignmentChangeRepository).save(argThat(change ->
                change.getWeeklySchedule().getId().equals(9L)
                        && change.getEmployee().getId().equals(EMPLOYEE_ID)
                        && change.getDate().equals(ASSIGNMENT_DATE)
                        && "CREATED".equals(change.getChangeType())
        ));
    }

    @Test
    void validateScheduleAssignmentReturnsAvailabilityWarningInsteadOfThrowing() {
        stubEmployeeLookup();
        when(availabilityRepository.count()).thenReturn(1L);
        when(availabilityRepository.existsByEmployee_Id(EMPLOYEE_ID)).thenReturn(true);
        when(availabilityRepository.findByEmployee_IdAndDate(EMPLOYEE_ID, ASSIGNMENT_DATE))
                .thenReturn(List.of(availability("BEFORE", LocalTime.of(12, 0))));

        ScheduleValidationResponse response = assertDoesNotThrow(
                () -> scheduleAssignmentService.validateScheduleAssignment(assignment)
        );

        assertTrue(response.getWarnings().contains("該員工這個時段無法上班"));
    }

    @Test
    void createScheduleAssignmentBlocksSameEmployeeTimeOverlap() {
        stubEmployeeLookup();
        stubNoSameDayAssignments();
        when(scheduleAssignmentRepository
                .findByEmployee_IdAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        EMPLOYEE_ID,
                        ASSIGNMENT_DATE,
                        assignment.getEndTime(),
                        assignment.getStartTime()
                ))
                .thenReturn(List.of(existingWorkAssignment()));

        assertThrows(
                RuntimeException.class,
                () -> scheduleAssignmentService.createScheduleAssignment(assignment)
        );
    }

    @Test
    void createScheduleAssignmentAllowsRestWithoutCheckingAvailability() {
        ScheduleAssignment restAssignment = restAssignment();
        stubEmployeeLookup();
        when(scheduleAssignmentRepository.findByEmployee_IdAndDate(EMPLOYEE_ID, ASSIGNMENT_DATE))
                .thenReturn(Collections.emptyList());
        stubSave();

        assertDoesNotThrow(() -> scheduleAssignmentService.createScheduleAssignment(restAssignment));

        verify(scheduleAssignmentRepository).save(restAssignment);
        verify(availabilityRepository, never()).count();
    }

    @Test
    void restAssignmentDoesNotCountTowardWorkHours() {
        ScheduleAssignment restAssignment = restAssignment();
        when(scheduleAssignmentRepository.findByEmployee_IdAndDateBetween(
                EMPLOYEE_ID,
                ASSIGNMENT_DATE,
                ASSIGNMENT_DATE
        ))
                .thenReturn(List.of(restAssignment));

        String result = scheduleAssignmentService.getEmployeeWorkHours(
                EMPLOYEE_ID,
                ASSIGNMENT_DATE,
                ASSIGNMENT_DATE
        );

        assertTrue(result.contains("0小時"));
    }

    @Test
    void restRequirementDoesNotAffectGapCheck() {
        when(positionRequirementRepository.findAll())
                .thenReturn(List.of(positionRequirement(restPosition(), 1)));

        List<String> result = scheduleAssignmentService.checkGaps(ASSIGNMENT_DATE);

        assertTrue(result.isEmpty());
        verify(scheduleAssignmentRepository, never()).findByDateAndPosition_Id(any(), any());
    }

    @Test
    void restRequirementDoesNotAffectOverstaffedCheck() {
        when(positionRequirementRepository.findAll())
                .thenReturn(List.of(positionRequirement(restPosition(), 0)));

        List<String> result = scheduleAssignmentService.checkOverstaffed(ASSIGNMENT_DATE);

        assertTrue(result.isEmpty());
        verify(scheduleAssignmentRepository, never()).findByDateAndPosition_Id(any(), any());
    }

    @Test
    void createScheduleAssignmentBlocksWorkWhenEmployeeAlreadyHasRestAssignment() {
        stubEmployeeLookup();
        when(scheduleAssignmentRepository.findByEmployee_IdAndDate(EMPLOYEE_ID, ASSIGNMENT_DATE))
                .thenReturn(List.of(restAssignment()));

        assertThrows(
                RuntimeException.class,
                () -> scheduleAssignmentService.createScheduleAssignment(assignment)
        );
    }

    @Test
    void updateScheduleAssignmentCanChangeExistingAssignmentToRest() {
        ScheduleAssignment existingAssignment = new ScheduleAssignment();
        existingAssignment.setId(10L);
        existingAssignment.setEmployee(employee);
        existingAssignment.setPosition(assignment.getPosition());
        existingAssignment.setDate(ASSIGNMENT_DATE);
        existingAssignment.setStartTime(LocalTime.of(10, 0));
        existingAssignment.setEndTime(LocalTime.of(14, 0));

        ScheduleAssignment newRestAssignment = restAssignment();

        when(scheduleAssignmentRepository.findById(10L))
                .thenReturn(Optional.of(existingAssignment));
        stubEmployeeLookup();
        when(scheduleAssignmentRepository.findByEmployee_IdAndDate(EMPLOYEE_ID, ASSIGNMENT_DATE))
                .thenReturn(List.of(existingAssignment));
        stubSave();

        assertDoesNotThrow(
                () -> scheduleAssignmentService.updateScheduleAssignment(10L, newRestAssignment)
        );

        verify(scheduleAssignmentRepository).save(existingAssignment);
    }

    private Availability availability(String availabilityType, LocalTime boundaryTime) {
        Availability availability = new Availability();
        availability.setEmployee(employee);
        availability.setDate(ASSIGNMENT_DATE);
        availability.setAvailabilityType(availabilityType);
        availability.setBoundaryTime(boundaryTime);
        return availability;
    }

    private ScheduleAssignment restAssignment() {
        ScheduleAssignment restAssignment = new ScheduleAssignment();
        restAssignment.setEmployee(employee);
        restAssignment.setPosition(restPosition());
        restAssignment.setDate(ASSIGNMENT_DATE);
        restAssignment.setStartTime(LocalTime.of(0, 0));
        restAssignment.setEndTime(LocalTime.of(0, 0));
        return restAssignment;
    }

    private ScheduleAssignment existingWorkAssignment() {
        ScheduleAssignment existingAssignment = new ScheduleAssignment();
        existingAssignment.setId(20L);
        existingAssignment.setEmployee(employee);
        existingAssignment.setPosition(assignment.getPosition());
        existingAssignment.setDate(ASSIGNMENT_DATE);
        existingAssignment.setStartTime(LocalTime.of(9, 0));
        existingAssignment.setEndTime(LocalTime.of(12, 0));
        return existingAssignment;
    }

    private Position restPosition() {
        Position position = new Position();
        position.setId(99L);
        position.setName("休");
        position.setIsRequired(false);
        return position;
    }

    private PositionRequirement positionRequirement(Position position, int requiredCount) {
        PositionRequirement requirement = new PositionRequirement();
        requirement.setPosition(position);
        requirement.setRequiredCount(requiredCount);
        requirement.setStartTime(LocalTime.of(10, 0));
        requirement.setEndTime(LocalTime.of(14, 0));
        return requirement;
    }

    private WeeklySchedule weeklySchedule(Long id, String status) {
        WeeklySchedule weeklySchedule = new WeeklySchedule();
        weeklySchedule.setId(id);
        weeklySchedule.setStatus(status);
        return weeklySchedule;
    }

    private void stubEmployeeLookup() {
        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
    }

    private void stubNoSameDayAssignments() {
        when(scheduleAssignmentRepository.findByEmployee_IdAndDate(EMPLOYEE_ID, ASSIGNMENT_DATE))
                .thenReturn(Collections.emptyList());
    }

    private void stubNoTimeOverlap() {
        when(scheduleAssignmentRepository
                .findByEmployee_IdAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        EMPLOYEE_ID,
                        ASSIGNMENT_DATE,
                        assignment.getEndTime(),
                        assignment.getStartTime()
                ))
                .thenReturn(Collections.emptyList());
    }

    private void stubSave() {
        when(scheduleAssignmentRepository.save(any(ScheduleAssignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }
}
