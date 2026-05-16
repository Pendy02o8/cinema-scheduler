package com.pendy.cinema_scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String jobTitle;

    private Boolean isActive;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}