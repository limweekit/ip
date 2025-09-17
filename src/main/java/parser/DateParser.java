package parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility for parsing and formatting date/time arguments used for commands.
 */
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

    /**
     * Parses a date/time string using a set of accepted formats.
     * Trims surrounding whitespace. If no date-time pattern matches, attempts date-only patterns.
     * and returns the result at the start of day (00:00). If none match, throws an exception.
     *
     * @param text Input to parse.
     * @return A {@link LocalDateTime} representing the parsed value.
     * @throws IllegalArgumentException If the input does not match any supported format.
     */
    public static LocalDateTime parseFlexibleDateTime(String text) {
        text = text.trim();
        for (DateTimeFormatter f : DATE_TIME_INPUTS) {
            try {
                return LocalDateTime.parse(text, f);
            }
            catch (DateTimeParseException ignored) {
            }
        }
        for (DateTimeFormatter f : DATE_ONLY_INPUTS) {
            try {
                return LocalDate.parse(text, f).atStartOfDay();
            }
            catch (DateTimeParseException ignored) {
            }
        }
        throw new IllegalArgumentException("Unrecognised date/time format: " + text);
    }
}
