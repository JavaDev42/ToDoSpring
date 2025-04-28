package com.example.demo.DTOConverter;

import com.example.demo.DTO.TaskDTO;
import com.example.demo.Entity.Task;

public class TaskMapper {

    public static Task convertToEntity(TaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setStatus(dto.getStatus());
        return task;
    }

    public static TaskDTO convertToDTO(Task entity) {
        TaskDTO dto = new TaskDTO();
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setDueDate(entity.getDueDate());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}