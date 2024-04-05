package ch.cern.todo.exception;

public class CategoryWithAssociatedTasksException extends RuntimeException {

    public CategoryWithAssociatedTasksException(String message) {
        super(message);
    }
}
