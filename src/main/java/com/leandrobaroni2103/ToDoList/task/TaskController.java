package com.leandrobaroni2103.ToDoList.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leandrobaroni2103.ToDoList.core.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody Task taskModel, HttpServletRequest request) {
    // if (taskModel.getTitle().length() > 50) {
    //   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Título de tarefa excede o limite");
    // }

    LocalDateTime currentDate = LocalDateTime.now();
    if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Data de inicio/término deve ser maior que a data atual.");
    }
    if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data de inicio deve ser maior que a data de término.");
    }
    var userId = request.getAttribute("userId");
    taskModel.setUserId((UUID) userId);
    Task task = this.taskRepository.save(taskModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(task);
  }

  @GetMapping("/")
  public ResponseEntity<List<Task>> list(HttpServletRequest request) {
    UUID id = UUID.fromString(request.getAttribute("userId").toString());
    List<Task> list = this.taskRepository.findByUserId(id);
    return ResponseEntity.status(HttpStatus.CREATED).body(list);
  }

  // localhots/tasks/id_da_task
  @PutMapping("/{id}")
  public ResponseEntity update(@RequestBody Task task, @PathVariable UUID id, HttpServletRequest request) {
    Task oldTask = this.taskRepository.findById(id).orElse(null);

    if (oldTask == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrado");
    }

    if (!oldTask.getUserId().equals(request.getAttribute("userId"))) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para está operação.");
    }
    Utils.copyNonNullProperties(task, oldTask);

    Task newTask = this.taskRepository.save(task);

    return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
  }
}
