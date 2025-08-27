package model;

import java.time.LocalDateTime;
import parser.DateParser;

/**
 * A task that contains a date specifying the task's deadline.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String serialize() {
        int done = isDone ? 1 : 0;
        String formatBy = by.getHour() == 0 && by.getMinute() == 0
                ? by.toLocalDate().toString()
                : by.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return String.format("D | %d | %s | %s", done, description, formatBy);
    }

    @Override
    public String toString() {
        String formatBy = by.getHour() == 0 && by.getMinute() == 0
                ? by.format(DateParser.OUT_DATE)
                : by.format(DateParser.OUT_DATE_TIME);
        return super.toString() + " (By: " + formatBy + ")";
    }
}