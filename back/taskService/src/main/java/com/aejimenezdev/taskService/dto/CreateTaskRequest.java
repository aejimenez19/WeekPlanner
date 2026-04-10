package com.aejimenezdev.taskService.dto;

import com.aejimenezdev.taskService.model.DayOfWeek;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Title es requerido")
    private String title;

    private String description;

    @NotNull(message = "Day of week es requerido")
    private DayOfWeek dayOfWeek;

    private LocalTime time;
}