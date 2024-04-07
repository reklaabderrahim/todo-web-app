package ch.cern.todo.service.impl;

import ch.cern.todo.exception.TaskNotFoundException;
import ch.cern.todo.model.Category;
import ch.cern.todo.model.Task;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void retrieveAllTasks() {
        // given
        String name = "name";
        String description = "description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        Category category = Category.create("categoryName", "CategoryDescription");
        Task task = Task.linkToCategory(Task.create(name, description, deadline), category);

        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));

        // when
        List<Task> tasks = taskService.retrieveAllTasks();

        // then
        assertThat(tasks.size()).isEqualTo(1);
        assertThat(tasks.get(0).getName()).isEqualTo(name);
        assertThat(tasks.get(0).getDescription()).isEqualTo(description);
        assertThat(tasks.get(0).getCategory()).isNotNull();

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void createTaskSuccess() {
        // given
        String name = "name";
        String description = "description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        Category category = Category.create("categoryName", "CategoryDescription");
        Task task = Task.create(name, description, deadline);

        when(categoryRepository.findCategoriesByName("categoryName")).thenReturn(Optional.of(category));
        when(taskRepository.saveAndFlush(task)).thenReturn(task);

        // when
        Task createdTask = taskService.createTask(task, category);

        // then
        assertThat(createdTask.getName()).isEqualTo(name);
        assertThat(createdTask.getDescription()).isEqualTo(description);
        assertThat(createdTask.getCategory()).isNotNull();

        verify(taskRepository, times(1)).saveAndFlush(task);
    }

    @Test
    public void updateTaskSuccess() {
        // given
        Long id = 1L;
        String name = "name";
        String description = "description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        Category category = Category.create("categoryName", "CategoryDescription");
        Task task = Task.create(id, name, description, deadline);

        when(taskRepository.existsById(task.getId())).thenReturn(true);
        when(categoryRepository.findCategoriesByName("categoryName")).thenReturn(Optional.of(category));
        when(taskRepository.saveAndFlush(task)).thenReturn(task);

        // when
        Task createdTask = taskService.updateTask(task, category);

        // then
        assertThat(createdTask.getName()).isEqualTo(name);
        assertThat(createdTask.getDescription()).isEqualTo(description);
        assertThat(createdTask.getCategory()).isNotNull();

        verify(taskRepository, times(1)).saveAndFlush(task);
    }

    @Test
    public void updateTaskNotFoundException() {
        // given
        Long id = 1L;
        String name = "name";
        String description = "description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        Category category = Category.create("categoryName", "CategoryDescription");
        Task task = Task.create(id, name, description, deadline);

        when(taskRepository.existsById(task.getId())).thenReturn(false);

        // when
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(task, category));

        verify(taskRepository, times(0)).saveAndFlush(task);
    }

    @Test
    public void findTaskSuccess() {
        // given
        Long id = 1L;
        String name = "name";
        String description = "description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        Category category = Category.create("categoryName", "CategoryDescription");
        Task task = Task.linkToCategory(Task.create(id, name, description, deadline), category);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        // when
        Task createdTask = taskService.findTaskById(task.getId());

        // then
        assertThat(createdTask.getName()).isEqualTo(name);
        assertThat(createdTask.getDescription()).isEqualTo(description);
        assertThat(createdTask.getCategory()).isNotNull();

        verify(taskRepository, times(1)).findById(task.getId());
    }

    @Test
    public void findTaskNotFoundException() {
        // given
        Long id = 1L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // then
        assertThrows(TaskNotFoundException.class, () -> taskService.findTaskById(id));
    }

    @Test
    public void deleteTaskSuccess() {
        // given
        Long id = 1L;

        when(taskRepository.existsById(id)).thenReturn(true);

        // when
        taskService.deleteTaskById(id);

        // then
        verify(taskRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteTaskNotFoundException() {
        // given
        Long id = 1L;

        when(taskRepository.existsById(id)).thenReturn(false);

        // when
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(id));

        // then
        verify(taskRepository, times(0)).deleteById(id);
    }
}