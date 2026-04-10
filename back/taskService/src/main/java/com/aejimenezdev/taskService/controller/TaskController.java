package com.aejimenezdev.taskService.controller;

import com.aejimenezdev.taskService.dto.CreateTaskRequest;
import com.aejimenezdev.taskService.dto.TaskResponse;
import com.aejimenezdev.taskService.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateTaskRequest request) {
        
        TaskResponse response = taskService.createTask(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}