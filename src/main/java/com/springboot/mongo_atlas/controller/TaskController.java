package com.springboot.mongo_atlas.controller;

import com.springboot.mongo_atlas.db1.entity.Task;
import com.springboot.mongo_atlas.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> findAllTasks() {
        return ResponseEntity.ok(taskService.findAllTask());
    }

    @PostMapping
    public ResponseEntity<Task> saveTask(@RequestBody Task task) {
        return ResponseEntity.status(201).body(taskService.saveTask(task));
    }

    @PutMapping
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        return ResponseEntity.status(200).body(taskService.updateTask(task));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable UUID taskId) {
        return ResponseEntity.ok(taskService.deleteById(taskId));
    }

    @GetMapping("/{assignee:[a-zA-Z ]*}")
    public ResponseEntity<List<Task>> findTaskByAssignee(@PathVariable String assignee) {
        return ResponseEntity.ok(taskService.findByAssignee(assignee));
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<Task>> findTaskBySeverity(@PathVariable int severity) {
        return ResponseEntity.ok(taskService.findBySeverity(severity));
    }

    @GetMapping("/id/{taskId}")
    public ResponseEntity<Task> findTaskById(@PathVariable UUID taskId) {
        return ResponseEntity.ok(taskService.findTaskById(taskId));
    }


}
