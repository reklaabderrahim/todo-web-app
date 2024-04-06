package ch.cern.todo.dto;

import ch.cern.todo.validator.ByteSize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryDto {

    @NotBlank(message = "Category name cannot be blank")
    @ByteSize(max = 100, message = "Category name byte size should not exceed 100 bytes")
    private String name;

    @NotBlank(message = "Category description cannot be blank")
    @ByteSize(max = 500, message = "Category description byte size should not exceed 500 bytes")
    private String description;
}
