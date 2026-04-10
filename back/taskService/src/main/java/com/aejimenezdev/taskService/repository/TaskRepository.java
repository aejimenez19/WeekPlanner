package com.aejimenezdev.taskService.repository;

import com.aejimenezdev.taskService.model.DayOfWeek;
import com.aejimenezdev.taskService.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndCompleted(Long userId, Boolean completed);

    List<Task> findByUserIdAndDayOfWeek(Long userId, DayOfWeek dayOfWeek);

    List<Task> findByUserIdAndCompletedAndDayOfWeek(Long userId, Boolean completed, DayOfWeek dayOfWeek);
}