package ch.cern.todo.service.impl;

import ch.cern.todo.exception.CategoryNotFoundException;
import ch.cern.todo.exception.TaskNotFoundException;
import ch.cern.todo.model.Category;
import ch.cern.todo.model.Task;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

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
    @Transactional
    public Task createTask(Task task, Category category) {
        Category foundCategory = categoryRepository.findCategoriesByName(category.getName())
                .orElse(category);
        return taskRepository.saveAndFlush(Task.linkToCategory(task, foundCategory));
    }

    @Override
    @Transactional
    public Task updateTask(Task task, Category category) {
        if (!taskRepository.existsById(task.getId())) {
            throw new CategoryNotFoundException("Cannot find task with id : " + task.getId());
        }
        Category foundCategory = categoryRepository.findCategoriesByName(category.getName())
                .orElse(category);

        return taskRepository.saveAndFlush(Task.linkToCategory(task, foundCategory));
    }

    @Override
    @Transactional
    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Cannot find task with id : " + id);
        }
        taskRepository.deleteById(id);
    }
}
