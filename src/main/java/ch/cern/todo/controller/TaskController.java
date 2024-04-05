package ch.cern.todo.controller;

import ch.cern.todo.dto.TaskDto;
import ch.cern.todo.model.Category;
import ch.cern.todo.model.Task;
import ch.cern.todo.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(
                taskService.createTask(
                        Task.create(taskDto.getName(), taskDto.getDescription(), taskDto.getDeadline()),
                        Category.create(taskDto.getCategoryDto().getName(), taskDto.getCategoryDto().getDescription())),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@RequestBody TaskDto taskDto, @PathVariable Long id) {
        return new ResponseEntity<>(
                taskService.updateTask(
                        Task.create(id, taskDto.getName(), taskDto.getDescription(), taskDto.getDeadline()),
                        Category.create(taskDto.getCategoryDto().getName(), taskDto.getCategoryDto().getDescription())),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findTaskById(@PathVariable Long id) {
        return new ResponseEntity<>(
                taskService.findTaskById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<Task>> findAllTasks() {
        return new ResponseEntity<>(
                taskService.retrieveAllTasks(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok().build();
    }
}
