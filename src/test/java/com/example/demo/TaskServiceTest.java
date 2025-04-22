package com.example.demo;

import com.example.demo.DTO.TaskDTO;
import com.example.demo.Entity.Task;
import com.example.demo.Entity.TaskStatus;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.Service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService();
        // Используйте метод установки репозитория или конструктор.
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
    }

    @Test
    void testGetAllTasks() {
        // Подготовка данных для теста.
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Title 1");
        task1.setDescription("Description 1");
        task1.setDueDate(LocalDateTime.now());
        task1.setStatus(TaskStatus.TODO);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1));

        // Вызов метода.
        var tasks = taskService.getAllTasks();

        // Проверка результатов.
        assertEquals(1, tasks.size());
        assertEquals("Title 1", tasks.get(0).getTitle());
        verify(taskRepository).findAll();
    }

    @Test
    void testGetTaskById() {
        // Подготовка данных для теста.
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Title 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Вызов метода.
        var result = taskService.getTaskById(1L);

        // Проверка результатов.
        assertTrue(result.isPresent());
        assertEquals("Title 1", result.get().getTitle());
    }

    @Test
    void testGetTaskById_NotFound() {
        // Подготовка данных для теста.

        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        // Вызов метода.
        var result = taskService.getTaskById(2L);

        // Проверка результатов.
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateTask() {
        // Подготовка данных для теста.
        TaskDTO newTaskDto = new TaskDTO();
        newTaskDto.setTitle("New Title");
        newTaskDto.setDescription("New Description");
        newTaskDto.setDueDate(LocalDateTime.now());
        newTaskDto.setStatus(TaskStatus.TODO);

        // Настройка мока для метода save
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(1L); // Установите ID для имитации сохраненной задачи
            return savedTask;
        });

        // Вызов метода.
        var createdTask = taskService.createTask(newTaskDto);

        // Проверка результатов.
        assertNotNull(createdTask); // Проверяем, что созданная задача не null
        assertEquals(1L, createdTask.getId()); // Проверяем, что ID установлен
        assertEquals("New Title", createdTask.getTitle()); // Проверяем заголовок

        verify(taskRepository).save(any(Task.class)); // Проверяем, что save был вызван
    }

    @Test
    void testUpdateTask() {
        // Подготовка данных для теста.
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setDueDate(LocalDateTime.now());
        existingTask.setStatus(TaskStatus.TODO);

        // Настройка мока для репозитория
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        TaskDTO updatedDto = new TaskDTO();
        updatedDto.setTitle("Updated Title");
        updatedDto.setDescription("Updated Description");
        updatedDto.setDueDate(LocalDateTime.now().plusDays(1));
        updatedDto.setStatus(TaskStatus.DONE); // Убедитесь, что статус передается как строка

        // Настройка мока для метода save
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(1L); // Установите ID для имитации сохраненной задачи
            return savedTask;
        });

        // Вызов метода обновления задачи
        var updatedTask = taskService.updateTask(1L, updatedDto);

        // Проверка результатов
        assertNotNull(updatedTask);
        assertEquals("Updated Title", updatedTask.getTitle());

        // Проверка, что изменения были применены к существующей задаче
        assertEquals("Updated Description", existingTask.getDescription());

        verify(taskRepository).save(existingTask);
    }

    @Test
    void testUpdateTask_NotFound() {
        // Подготовка данных для теста.

        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        TaskDTO updatedDto = new TaskDTO();
        updatedDto.setTitle("Updated Title");

        // Вызов метода обновления задачи
        var updatedResult = taskService.updateTask(2L, updatedDto);

        // Проверка результатов
        assertNull(updatedResult);
    }

    @Test
    void testDeleteTask() {
        Long idToDelete = 1L;

        doNothing().when(taskRepository).deleteById(idToDelete);

        // Вызов метода удаления задачи
        taskService.deleteTask(idToDelete);

        verify(taskRepository).deleteById(idToDelete);
    }

    @Test
    void testFilterTasksByStatus() {
        // Подготовка данных для теста.
        Task activeTask = new Task();
        activeTask.setId(1L);
        activeTask.setTitle("Active Task");
        activeTask.setStatus(TaskStatus.TODO);

        when(taskRepository.findByStatus(TaskStatus.TODO)).thenReturn(Arrays.asList(activeTask));

        // Вызов метода фильтрации задач по статусу
        var filteredTasks = taskService.filterTasksByStatus(TaskStatus.TODO);

        // Проверка результатов
        assertEquals(1, filteredTasks.size());
        assertEquals("Active Task", filteredTasks.get(0).getTitle());
    }
}