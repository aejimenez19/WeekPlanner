package com.aejimenezdev.taskService.dto;

import com.aejimenezdev.taskService.model.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private DayOfWeek dayOfWeek;
    private LocalTime time;
    private Boolean completed;
    private Long userId;
}