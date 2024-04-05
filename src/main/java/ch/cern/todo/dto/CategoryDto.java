package ch.cern.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {

    @NotBlank(message = "Category name cannot be blank")
    private String name;
    @NotBlank(message = "Category description cannot be blank")
    private String description;
}
