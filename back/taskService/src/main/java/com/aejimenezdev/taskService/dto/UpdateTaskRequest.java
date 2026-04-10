package com.aejimenezdev.taskService.dto;

import com.aejimenezdev.taskService.model.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateTaskRequest {
    private String title;

    private String description;

    private DayOfWeek dayOfWeek;

    private LocalTime time;

    private Boolean completed;
}