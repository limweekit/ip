public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String serialize() {
        int done = isDone ? 1 : 0;
        return String.format("D | %d | %s | %s", done, description, by);
    }

    @Override
    public String toString() {
        return super.toString() + " (By: " + by + ")";
    }
}