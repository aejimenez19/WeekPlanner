package com.aejimenezdev.taskService.service;

import com.aejimenezdev.taskService.dto.CreateTaskRequest;
import com.aejimenezdev.taskService.dto.UpdateTaskRequest;
import com.aejimenezdev.taskService.dto.TaskResponse;
import com.aejimenezdev.taskService.model.DayOfWeek;
import com.aejimenezdev.taskService.model.Task;
import com.aejimenezdev.taskService.repository.TaskRepository;
import com.aejimenezdev.taskService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(CreateTaskRequest request, Long userId) {
        log.info("[TaskService] Creando tarea para usuario ID: {} - Título: {}", userId, request.getTitle());
        
        if (!userRepository.existsById(userId)) {
            log.warn("[TaskService] Usuario no encontrado ID: {}", userId);
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
        
        log.info("[TaskService] Tarea creada ID: {} para usuario ID: {}", savedTask.getId(), userId);

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
        log.info("[TaskService] Obteniendo tareas - Usuario ID: {}, Día: {}, Completado: {}", 
                userId, dayOfWeek, completed);
        
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

        log.info("[TaskService] Se encontraron {} tareas para usuario ID: {}", tasks.size(), userId);
        
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
        log.info("[TaskService] Actualizando tarea ID: {} para usuario ID: {}", taskId, userId);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("[TaskService] Tarea no encontrada ID: {}", taskId);
                    return new RuntimeException("Tarea no encontrada");
                });

        if (!task.getUserId().equals(userId)) {
            log.warn("[TaskService] Permiso denegado - usuario {} intentó actualizar tarea {}", userId, taskId);
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
        
        log.info("[TaskService] Tarea ID: {} actualizada exitosamente", taskId);

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

    public void deleteTask(Long taskId, Long userId) {
        log.info("[TaskService] Eliminando tarea ID: {} para usuario ID: {}", taskId, userId);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("[TaskService] Tarea no encontrada ID: {}", taskId);
                    return new RuntimeException("Tarea no encontrada");
                });

        if (!task.getUserId().equals(userId)) {
            log.warn("[TaskService] Permiso denegado - usuario {} intentó eliminar tarea {}", userId, taskId);
            throw new SecurityException("No tienes permiso para eliminar esta tarea");
        }

        taskRepository.delete(task);
        
        log.info("[TaskService] Tarea ID: {} eliminada exitosamente", taskId);
    }
}