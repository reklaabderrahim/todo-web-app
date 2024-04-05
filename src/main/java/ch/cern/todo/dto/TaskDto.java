package ch.cern.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {
    @NotBlank(message = "Task name cannot be blank")
    private String name;

    @NotBlank(message = "Task description cannot be blank")
    private String description;

    @Future(message = "Task deadline should be in the future")
    @NotNull(message = "Task deadline cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deadline;

    @Valid
    @NotNull(message = "Task category cannot be null")
    @JsonProperty("category")
    private CategoryDto categoryDto;
}
