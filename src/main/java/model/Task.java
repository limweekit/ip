package model;

public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public String serialize() {
        int done = isDone ? 1 : 0;
        return String.format("%s | %d | %s", type.getSymbol(), done, description);
    }

    @Override
    public String toString() {
        return "[" + type.getSymbol() + "] [" + getStatusIcon() + "] " + description;
    }
}

