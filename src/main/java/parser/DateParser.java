package parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateParser {
    // Accepted inputs
    private static final DateTimeFormatter[] DATE_TIME_INPUTS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    };

    private static final DateTimeFormatter[] DATE_ONLY_INPUTS = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("MMM dd yyyy")
    };

    // Output formats
    public static final DateTimeFormatter OUT_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    public static final DateTimeFormatter OUT_DATE_TIME = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    public static LocalDateTime parseFlexibleDateTime(String text) {
        text = text.trim();
        for (DateTimeFormatter f : DATE_TIME_INPUTS) {
            try {
                return LocalDateTime.parse(text, f);
            }
            catch (DateTimeParseException ignored) {}
        }

        for (DateTimeFormatter f : DATE_ONLY_INPUTS) {
            try {
                return LocalDate.parse(text, f).atStartOfDay();
            }
            catch (DateTimeParseException ignored) {}
        }
        throw new IllegalArgumentException("Unrecognised date/time format: " + text);
    }
}
