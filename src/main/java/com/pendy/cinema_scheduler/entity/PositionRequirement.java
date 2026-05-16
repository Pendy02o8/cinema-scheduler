package com.pendy.cinema_scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "position_requirements")
public class PositionRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "weekly_schedule_id")
    private WeeklySchedule weeklySchedule;

    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Positions position;

    @Column(name = "required_count")
    private Integer requiredCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}