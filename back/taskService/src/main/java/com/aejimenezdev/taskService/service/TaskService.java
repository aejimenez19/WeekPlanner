package com.aejimenezdev.taskService.service;

import com.aejimenezdev.taskService.dto.CreateTaskRequest;
import com.aejimenezdev.taskService.dto.UpdateTaskRequest;
import com.aejimenezdev.taskService.dto.TaskResponse;
import com.aejimenezdev.taskService.model.DayOfWeek;
import com.aejimenezdev.taskService.model.Task;
import com.aejimenezdev.taskService.repository.TaskRepository;
import com.aejimenezdev.taskService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(CreateTaskRequest request, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dayOfWeek(request.getDayOfWeek())
                .time(request.getTime())
                .completed(false)
                .userId(userId)
                .build();

        Task savedTask = taskRepository.save(task);

        return TaskResponse.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .dayOfWeek(savedTask.getDayOfWeek())
                .time(savedTask.getTime())
                .completed(savedTask.getCompleted())
                .userId(savedTask.getUserId())
                .build();
    }

    public List<TaskResponse> getTasks(Long userId, Boolean completed, DayOfWeek dayOfWeek) {
        List<Task> tasks;

        if (completed != null && dayOfWeek != null) {
            tasks = taskRepository.findByUserIdAndCompletedAndDayOfWeek(userId, completed, dayOfWeek);
        } else if (completed != null) {
            tasks = taskRepository.findByUserIdAndCompleted(userId, completed);
        } else if (dayOfWeek != null) {
            tasks = taskRepository.findByUserIdAndDayOfWeek(userId, dayOfWeek);
        } else {
            tasks = taskRepository.findByUserId(userId);
        }

        return tasks.stream()
                .map(task -> TaskResponse.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .dayOfWeek(task.getDayOfWeek())
                        .time(task.getTime())
                        .completed(task.getCompleted())
                        .userId(task.getUserId())
                        .build())
                .toList();
    }

    public TaskResponse updateTask(Long taskId, Long userId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (!task.getUserId().equals(userId)) {
            throw new SecurityException("No tienes permiso para actualizar esta tarea");
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getDayOfWeek() != null) {
            task.setDayOfWeek(request.getDayOfWeek());
        }
        if (request.getTime() != null) {
            task.setTime(request.getTime());
        }
        if (request.getCompleted() != null) {
            task.setCompleted(request.getCompleted());
        }

        Task updatedTask = taskRepository.save(task);

        return TaskResponse.builder()
                .id(updatedTask.getId())
                .title(updatedTask.getTitle())
                .description(updatedTask.getDescription())
                .dayOfWeek(updatedTask.getDayOfWeek())
                .time(updatedTask.getTime())
                .completed(updatedTask.getCompleted())
                .userId(updatedTask.getUserId())
                .build();
    }
}