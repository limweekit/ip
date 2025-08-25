public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    @Override
    public String serialize() {
        int done = isDone ? 1 : 0;
        return String.format("E | %d | %s | %s | %s", done, description, from, to);
    }

    @Override
    public String toString() {
        return super.toString() + " (From: " + this.from + " To: " + this.to + ")";
    }
}
