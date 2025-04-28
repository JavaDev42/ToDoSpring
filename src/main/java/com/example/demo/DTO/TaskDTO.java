package com.example.demo.DTO;

import com.example.demo.Entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus status;
}