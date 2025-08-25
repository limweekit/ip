import java.time.LocalDateTime;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String serialize() {
        int done = isDone ? 1 : 0;
        String formatFrom = from.getHour()==0 && from.getMinute()==0
                ? from.toLocalDate().toString()
                : from.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String formatTo = to.getHour()==0 && to.getMinute()==0
                ? to.toLocalDate().toString()
                : to.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return String.format("E | %d | %s | %s | %s", done, description, formatFrom, formatTo);
    }

    @Override
    public String toString() {
        String formatFrom = from.getHour() == 0 && from.getMinute() == 0
                ? from.format(DateParser.OUT_DATE)
                : from.format(DateParser.OUT_DATE_TIME);

        String formatTo = to.getHour() == 0 && to.getMinute() == 0
                ? to.format(DateParser.OUT_DATE)
                : to.format(DateParser.OUT_DATE_TIME);

        return super.toString() + " (From: " + formatFrom + " To: " + formatTo + ")";
    }
}

