package com.example.demo.Repository;

import com.example.demo.Entity.Task;
import com.example.demo.Entity.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);
    Optional<Task> findByTitle(String title);
}