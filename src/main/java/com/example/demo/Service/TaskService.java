package com.example.demo.Service;

import com.example.demo.DTO.TaskDTO;
import com.example.demo.DTOConverter.TaskMapper;
import com.example.demo.Entity.TaskStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.Entity.Task;
import com.example.demo.Repository.TaskRepository;
import java.util.List;
@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::convertToDTO)
                .toList();
    }

    public TaskDTO getTaskByTitle(String title) {
        return taskRepository.findByTitle(title)
                .map(TaskMapper::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = TaskMapper.convertToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.convertToDTO(savedTask);
    }

    public TaskDTO updateTaskByTitle(String title, TaskDTO taskDTO) {
        return taskRepository.findByTitle(title)
                .map(existingTask -> {
                    existingTask.setTitle(taskDTO.getTitle());
                    existingTask.setDescription(taskDTO.getDescription());
                    existingTask.setDueDate(taskDTO.getDueDate());
                    existingTask.setStatus(taskDTO.getStatus());
                    return TaskMapper.convertToDTO(taskRepository.save(existingTask));
                }).orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }

    public void deleteTaskByTitle(String title) {
        Task task = taskRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        taskRepository.delete(task);
    }

    public List<TaskDTO> filterTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .map(TaskMapper::convertToDTO)
                .toList();
    }
}