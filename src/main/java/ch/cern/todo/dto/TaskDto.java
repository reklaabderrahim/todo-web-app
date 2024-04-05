package ch.cern.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {
    private String name;
    private String description;
    private LocalDateTime deadline;

    @JsonProperty("category")
    private CategoryDto categoryDto;
}
