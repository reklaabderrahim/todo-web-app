package ch.cern.todo.exception;

public class CategoryAlreadyExistException extends RuntimeException {

    public CategoryAlreadyExistException(String message) {
        super(message);
    }
}
