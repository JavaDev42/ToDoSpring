package com.example.demo.Controller;

import com.example.demo.DTO.TaskDTO;
import com.example.demo.Entity.Task;
import com.example.demo.Service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{title}")
    public ResponseEntity<TaskDTO> getTaskByTitle(@PathVariable String title) {
        try {
            return ResponseEntity.ok(taskService.getTaskByTitle(title));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO taskDto) {
        return taskService.createTask(taskDto);
    }

    @PutMapping("{title}")
    public ResponseEntity<TaskDTO> updateTaskByTitle(@PathVariable String title, @RequestBody TaskDTO taskDto) {
        try {
            return ResponseEntity.ok(taskService.updateTaskByTitle(title, taskDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{title}")
    public ResponseEntity<Void> deleteTaskByTitle(@PathVariable String title) {
        try {
            taskService.deleteTaskByTitle(title);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}