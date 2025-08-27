package model;

/**
 * Represents a task with a description, completion status and a {@link TaskType}.
 * This is the base class for concrete tasks such as {@link ToDo}, {@link Deadline}, and {@link Event}.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    /**
     * Constructor for a new task with the given description and type.
     *
     * @param description A short description or name of the task.
     * @param type The TaskType of the task.
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Returns the completion status of the task.
     *
     * @return {@code "X"} if task is done; {@code " "} otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Serializes this task to a single line for persistence.
     * Base format (subclasses append extra fields): {@code "<TYPE> | <DONE> | <DESCRIPTION>"}.
     *
     * @return The serialized representation of the task.
     */
    public String serialize() {
        int done = isDone ? 1 : 0;
        return String.format("%s | %d | %s", type.getSymbol(), done, description);
    }

    /**
     * Returns a human-readable representation of the task.
     */
    @Override
    public String toString() {
        return "[" + type.getSymbol() + "] [" + getStatusIcon() + "] " + description;
    }
}

