package com.example.demo;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.DTO.TaskDTO;
import com.example.demo.Entity.Task;
import com.example.demo.Entity.TaskStatus;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.Service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    public void setUp() {
        task = new Task();
        task.setTitle("My Task");
        task.setDescription("Task Description");
        task.setDueDate(LocalDateTime.now());
        task.setStatus(TaskStatus.DONE);

        taskDTO = new TaskDTO();
        taskDTO.setTitle("My Task");
        taskDTO.setDescription("Task Description");
        taskDTO.setDueDate(LocalDateTime.now());
        taskDTO.setStatus(TaskStatus.DONE);
    }

    @Test
    public void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));

        List<TaskDTO> tasks = taskService.getAllTasks();

        assertEquals(1, tasks.size());
        assertEquals(task.getTitle(), tasks.get(0).getTitle());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testGetTaskByTitle_Success() {
        when(taskRepository.findByTitle("My Task")).thenReturn(Optional.of(task));

        TaskDTO foundTask = taskService.getTaskByTitle("My Task");

        assertEquals(task.getTitle(), foundTask.getTitle());

        verify(taskRepository, times(1)).findByTitle("My Task");
    }

    @Test
    public void testGetTaskByTitle_NotFound() {
        when(taskRepository.findByTitle("Nonexistent Task")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            taskService.getTaskByTitle("Nonexistent Task");
        });

        assertEquals("Task not found", exception.getMessage());

        verify(taskRepository, times(1)).findByTitle("Nonexistent Task");
    }

    @Test
    public void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO createdTask = taskService.createTask(taskDTO);

        assertEquals(task.getTitle(), createdTask.getTitle());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTaskByTitle_Success() {
        when(taskRepository.findByTitle("My Task")).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO updatedTask = new TaskDTO();
        updatedTask.setTitle("Updated Title");

        TaskDTO result = taskService.updateTaskByTitle("My Task", updatedTask);

        assertEquals(updatedTask.getTitle(), result.getTitle());

        verify(taskRepository, times(1)).findByTitle("My Task");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTaskByTitle_NotFound() {
        when(taskRepository.findByTitle("Nonexistent Task")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            taskService.updateTaskByTitle("Nonexistent Task", taskDTO);
        });

        assertEquals("Task not found", exception.getMessage());

        verify(taskRepository, times(1)).findByTitle("Nonexistent Task");
    }

    @Test
    public void testDeleteTaskByTitle_Success() {
        when(taskRepository.findByTitle("My Task")).thenReturn(Optional.of(task));

        assertDoesNotThrow(() -> {
            taskService.deleteTaskByTitle("My Task");
        });

        verify(taskRepository, times(1)).delete(any(Task.class));
    }

    @Test
    public void testDeleteTaskByTitle_NotFound() {
        when(taskRepository.findByTitle("Nonexistent Task")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            taskService.deleteTaskByTitle("Nonexistent Task");
        });

        assertEquals("Task not found", exception.getMessage());

        verify(taskRepository, times(1)).findByTitle("Nonexistent Task");
    }

    @Test
    public void testFilterTasksByStatus() {
        when(taskRepository.findByStatus(TaskStatus.DONE)).thenReturn(Collections.singletonList(task));

        List<TaskDTO> filteredTasks = taskService.filterTasksByStatus(TaskStatus.DONE);

        assertEquals(1, filteredTasks.size());
        assertEquals(task.getStatus(), filteredTasks.get(0).getStatus());

        verify(taskRepository, times(1)).findByStatus(TaskStatus.DONE);
    }
}