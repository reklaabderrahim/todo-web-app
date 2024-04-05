package ch.cern.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class Error {
    private LocalDateTime timestamp;
    private Integer code;
    private List<String> message;
}
