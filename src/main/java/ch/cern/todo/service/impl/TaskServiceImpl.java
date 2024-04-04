package ch.cern.todo.service.impl;

import ch.cern.todo.exception.TaskNotFoundException;
import ch.cern.todo.model.Task;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Cannot find task with id : " + id));
    }

    @Override
    public List<Task> retrieveAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task persistTask(Task task) {
        return taskRepository.saveAndFlush(task);
    }

    @Override
    public Long deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Cannot find task with id : " + id);
        }
        taskRepository.deleteById(id);
        return id;
    }
}
