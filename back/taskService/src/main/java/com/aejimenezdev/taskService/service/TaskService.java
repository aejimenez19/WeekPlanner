package com.aejimenezdev.taskService.service;

import com.aejimenezdev.taskService.dto.CreateTaskRequest;
import com.aejimenezdev.taskService.dto.TaskResponse;
import com.aejimenezdev.taskService.model.Task;
import com.aejimenezdev.taskService.repository.TaskRepository;
import com.aejimenezdev.taskService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}