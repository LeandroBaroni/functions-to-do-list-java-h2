package com.leandrobaroni2103.ToDoList.task;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<Task, UUID> {
  List<Task> findByUserId(UUID userId);

  Task findByIdAndUserId(UUID id, UUID userId);
}
