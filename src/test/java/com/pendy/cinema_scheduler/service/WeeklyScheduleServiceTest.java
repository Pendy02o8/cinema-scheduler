package com.pendy.cinema_scheduler.service;

import com.pendy.cinema_scheduler.entity.WeeklySchedule;
import com.pendy.cinema_scheduler.repository.AvailabilityRepository;
import com.pendy.cinema_scheduler.repository.BusinessHourRepository;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentChangeRepository;
import com.pendy.cinema_scheduler.repository.ScheduleAssignmentRepository;
import com.pendy.cinema_scheduler.repository.WeeklyScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeeklyScheduleServiceTest {

    @Mock
    private WeeklyScheduleRepository weeklyScheduleRepository;

    @Mock
    private ScheduleAssignmentRepository scheduleAssignmentRepository;

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private BusinessHourRepository businessHourRepository;

    @Mock
    private ScheduleAssignmentChangeRepository scheduleAssignmentChangeRepository;

    @InjectMocks
    private WeeklyScheduleService weeklyScheduleService;

    @Test
    void updateWeeklyScheduleClearsChangesWhenPublishedScheduleReturnsToDraft() {
        WeeklySchedule existing = weeklySchedule(9L, "PUBLISHED");
        WeeklySchedule update = weeklySchedule(null, "DRAFT");
        when(weeklyScheduleRepository.findById(9L)).thenReturn(Optional.of(existing));
        when(weeklyScheduleRepository.save(any(WeeklySchedule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        weeklyScheduleService.updateWeeklySchedule(9L, update);

        verify(scheduleAssignmentChangeRepository).deleteByWeeklySchedule_Id(9L);
        verify(scheduleAssignmentRepository, never()).deleteByWeeklySchedule_Id(9L);
    }

    @Test
    void updateWeeklyScheduleDoesNotClearChangesWhenPublishingDraftSchedule() {
        WeeklySchedule existing = weeklySchedule(9L, "DRAFT");
        WeeklySchedule update = weeklySchedule(null, "PUBLISHED");
        when(weeklyScheduleRepository.findById(9L)).thenReturn(Optional.of(existing));
        when(weeklyScheduleRepository.save(any(WeeklySchedule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        weeklyScheduleService.updateWeeklySchedule(9L, update);

        verify(scheduleAssignmentChangeRepository, never()).deleteByWeeklySchedule_Id(9L);
    }

    private WeeklySchedule weeklySchedule(Long id, String status) {
        WeeklySchedule weeklySchedule = new WeeklySchedule();
        weeklySchedule.setId(id);
        weeklySchedule.setWeekStartDate(LocalDate.of(2026, 6, 15));
        weeklySchedule.setWeekEndDate(LocalDate.of(2026, 6, 21));
        weeklySchedule.setStatus(status);
        return weeklySchedule;
    }
}
