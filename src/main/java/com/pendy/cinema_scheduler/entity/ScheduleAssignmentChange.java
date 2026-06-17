package com.pendy.cinema_scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "schedule_assignment_changes")
public class ScheduleAssignmentChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "weekly_schedule_id")
    private WeeklySchedule weeklySchedule;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate date;

    @Column(name = "change_type")
    private String changeType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}