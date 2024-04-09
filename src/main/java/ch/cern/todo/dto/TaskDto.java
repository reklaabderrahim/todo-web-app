package ch.cern.todo.dto;

import ch.cern.todo.validator.ByteSize;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TaskDto {
    @NotBlank(message = "Task name cannot be blank")
    @ByteSize(max = 100, message = "Task name byte size should not exceed 100 bytes")
    private String name;

    @ByteSize(max = 500, message = "Task description byte size should not exceed 500 bytes")
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
