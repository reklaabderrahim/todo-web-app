package ch.cern.todo.controller;

import ch.cern.todo.dto.CategoryDto;
import ch.cern.todo.dto.TaskDto;
import ch.cern.todo.exception.TaskNotFoundException;
import ch.cern.todo.model.Category;
import ch.cern.todo.model.Task;
import ch.cern.todo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TaskService taskService;

    @Test
    void createTaskSuccess() throws Exception {
        // given
        String taskName = "Task";
        String taskDescription = "Task description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        Category category = Category.create(null, "category name", "category description");
        Task task = Task.create(null, taskName, taskDescription, deadline);
        TaskDto taskDto = getTaskDto(category, taskName, taskDescription, deadline);

        Mockito.when(taskService.createTask(task, category)).thenReturn(task);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(task)));
    }

    @Test
    void createTaskDeadlineShouldBeInTheFuture() throws Exception {
        // given
        String name = "Task";
        String taskDescription = "Task description";
        LocalDateTime deadline = LocalDateTime.now().minusMonths(1);
        Category category = Category.create(null, "category name", "category description");
        Task task = Task.create(null, name, taskDescription, deadline);
        TaskDto taskDto = getTaskDto(category, name, taskDescription, deadline);
        Mockito.when(taskService.createTask(task, category)).thenReturn(task);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Task deadline should be in the future")));
    }

    @Test
    void createTaskNameAndCategoryDescriptionValidationFailed() throws Exception {
        // given
        String taskDescription = "Task description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        Category category = Category.create(null, "category name", null); // description null
        Task task = Task.create(null, null, taskDescription, deadline);
        TaskDto taskDto = getTaskDto(category, null, taskDescription, deadline); // name null
        Mockito.when(taskService.createTask(task, category)).thenReturn(task);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Task name cannot be blank")))
                .andExpect(content().string(Matchers.containsString("Category description cannot be blank")));
    }

    @Test
    void updateTaskSuccess() throws Exception {
        // given
        String taskName = "Task";
        String taskDescription = "Task description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        Category category = Category.create(null, "category name", "category description");
        Task task = Task.create(1L, taskName, taskDescription, deadline);
        TaskDto taskDto = getTaskDto(category, taskName, taskDescription, deadline);

        Mockito.when(taskService.updateTask(task, category)).thenReturn(task);

        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(task)));
    }

    @Test
    void updateTaskNotFoundException() throws Exception {
        // given
        String taskName = "Task";
        String taskDescription = "Task description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        Category category = Category.create(null, "category name", "category description");
        Task task = Task.create(1L, taskName, taskDescription, deadline);
        TaskDto taskDto = getTaskDto(category, taskName, taskDescription, deadline);

        Mockito.when(taskService.updateTask(task, category))
                .thenThrow(new TaskNotFoundException("Cannot find task with id : " + task.getId()));

        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Cannot find task with id : " + task.getId())));
    }

    @Test
    void findTaskSuccess() throws Exception {
        // given
        String taskName = "Task";
        String taskDescription = "Task description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        Task task = Task.create(null, taskName, taskDescription, deadline);

        Mockito.when(taskService.findTaskById(1L)).thenReturn(task);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(task)));
    }

    @Test
    void findAllTasksSuccess() throws Exception {
        // given
        String taskName = "Task";
        String taskDescription = "Task description";
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        List<Task> tasks = Collections.singletonList(Task.create(null, taskName, taskDescription, deadline));

        Mockito.when(taskService.retrieveAllTasks()).thenReturn(tasks);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(tasks)));
    }

    @Test
    void deleteTaskSuccess() throws Exception {
        // given
        Mockito.doNothing().when(taskService).deleteTaskById(1L);

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private static TaskDto getTaskDto(Category category, String name, String taskDescription, LocalDateTime deadline) {
        CategoryDto categoryDto = CategoryDto.builder()
                .name(category.getName())
                .description(category.getDescription())
                .build();
        return TaskDto.builder()
                .name(name)
                .description(taskDescription)
                .deadline(deadline)
                .categoryDto(categoryDto)
                .build();
    }
}