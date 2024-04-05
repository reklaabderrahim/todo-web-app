package ch.cern.todo.handler;

import ch.cern.todo.dto.Error;
import ch.cern.todo.exception.*;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            CategoryNotFoundException.class, CategoryAlreadyExistException.class,
            CategoryWithAssociatedTasksException.class, TaskNotFoundException.class, TaskAlreadyExistException.class})
    protected ResponseEntity<Error> handleBusinessException(RuntimeException ex) {
        Error error = new Error(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), Collections.singletonList(ex.getMessage()));
        return ResponseEntity.badRequest().body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        Error error = new Error(
                LocalDateTime.now(),
                ex.getStatusCode().value(),
                ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList()
        );

        return ResponseEntity.badRequest().body(error);
    }
}
