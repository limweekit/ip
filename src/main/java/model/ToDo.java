package model;

/**
 * A task that can be marked as done.
 */
public class ToDo extends Task {
    public ToDo(String description) {
        super(description, TaskType.TODO);
    }
}
