package ch.cern.todo.service;

import ch.cern.todo.model.Task;

import java.util.List;

public interface TaskService {
    Task findTaskById(Long id);
    List<Task> retrieveAllTasks();
    Task persistTask(Task task);
    Long deleteTaskById(Long id);
}
