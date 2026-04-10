package com.aejimenezdev.taskService.controller;

import com.aejimenezdev.taskService.dto.CreateTaskRequest;
import com.aejimenezdev.taskService.dto.TaskResponse;
import com.aejimenezdev.taskService.dto.UpdateTaskRequest;
import com.aejimenezdev.taskService.model.DayOfWeek;
import com.aejimenezdev.taskService.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) DayOfWeek day) {
        
        List<TaskResponse> tasks = taskService.getTasks(userId, completed, day);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateTaskRequest request) {
        
        TaskResponse response = taskService.createTask(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        
        TaskResponse response = taskService.updateTask(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {
        
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }
}