package ch.cern.todo.service;

import ch.cern.todo.model.Category;
import ch.cern.todo.model.Task;

import java.util.List;

public interface TaskService {
    Task findTaskById(Long id);
    List<Task> retrieveAllTasks();
    Task createTask(Task task, Category category);
    Task updateTask(Task task, Category category);
    void deleteTaskById(Long id);
}
