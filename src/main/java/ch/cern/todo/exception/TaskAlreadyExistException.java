package ch.cern.todo.exception;

public class TaskAlreadyExistException extends RuntimeException {

    public TaskAlreadyExistException(String message) {
        super(message);
    }
}
