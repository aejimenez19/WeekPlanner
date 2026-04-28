package com.aejimenezdev.taskService.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UpdateTaskRequest {
    private String title;

    private String description;

    private LocalDate executionDate;

    private LocalTime time;

    private Boolean completed;
}