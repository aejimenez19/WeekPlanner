package com.aejimenezdev.taskService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Title es requerido")
    private String title;

    private String description;

    private LocalDate executionDate;

    private LocalTime time;
}