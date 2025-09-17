package parser;

import commands.*;
import exceptions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Parses raw user input into {@link Command} objects.
 * Uses a single dispatch on the first word and delegates the argument parsing to helper methods.
 */
public class Parser {
    /**
     * Parses a raw input line into a {@link Command}.
     *
     * @param input Raw user input.
     * @return A command representing the requested action.
     * @throws YapGPTException If parsing fails (empty input, invalid date, command, or missing arguments).
     */
    public static Command parse(String input) throws YapGPTException {
        assert input != null : "Input must not be null";

        input = input.trim();
        if (input.isEmpty()) {
            throw new UnknownCommandException("(empty)");
        }

        String[] tokens = input.split("\\s+", 2);
        String cmd = tokens[0].toLowerCase();
        String rest = tokens.length > 1 ? tokens[1] : "";

        return switch (cmd) {
            case "bye"     -> new ExitCommand();
            case "list"    -> new ListCommand();
            case "todo"    -> parseTodo(rest);
            case "deadline"-> parseDeadline(rest);
            case "event"   -> parseEvent(rest);
            case "mark"    -> new MarkCommand(parseIndex("mark", rest));
            case "unmark"  -> new UnmarkCommand(parseIndex("unmark", rest));
            case "delete"  -> new DeleteCommand(parseIndex("delete", rest));
            case "on"      -> parseOn(rest);
            case "find"    -> parseFind(rest);
            case "stats"   -> new StatsCommand();
            default        -> throw new UnknownCommandException(input);
        };
    }

    // -----------------------------------------------Helper functions-------------------------------------------------

    private static Command parseTodo(String rest) throws EmptyDescriptionException {
        String desc = requireNonEmpty("todo", rest);
        return new AddTodoCommand(desc);
    }

    private static Command parseDeadline(String rest) throws YapGPTException {
        String body = requireNonEmpty("deadline", rest);

        String[] p = body.split("/by", 2);
        String desc = p[0].trim();
        if (desc.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }
        if (p.length < 2 || p[1].trim().isEmpty()) {
            throw new InvalidDateException("deadline", "(missing)");
        }

        String rawBy = p[1].trim();
        final LocalDateTime by;
        try {
            by = DateParser.parseFlexibleDateTime(rawBy);
        } catch (IllegalArgumentException ex) {
            throw new InvalidDateException("deadline", rawBy);
        }

        assert by != null : "deadline: 'by' must not be null";
        return new AddDeadlineCommand(desc, by);
    }

    private static Command parseEvent(String rest) throws YapGPTException {
        String body = requireNonEmpty("event", rest);

        String[] a = body.split("/from", 2);
        String desc = a[0].trim();
        if (desc.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }
        if (a.length < 2 || a[1].trim().isEmpty()) {
            throw new InvalidDateException("event start", "(missing)");
        }

        String[] b = a[1].split("/to", 2);
        String fromRaw = b[0].trim();
        if (fromRaw.isEmpty()) {
            throw new InvalidDateException("event start", "(missing)");
        }
        if (b.length < 2 || b[1].trim().isEmpty()) {
            throw new InvalidDateException("event end", "(missing)");
        }

        final LocalDateTime from, to;

        try {
            from = DateParser.parseFlexibleDateTime(fromRaw);
        } catch (IllegalArgumentException e) {
            throw new InvalidDateException("event start", fromRaw);
        }

        String toRaw = b[1].trim();
        try {
            to = DateParser.parseFlexibleDateTime(toRaw);
        } catch (IllegalArgumentException e) {
            throw new InvalidDateException("event end", toRaw);
        }

        if (to.isBefore(from)) {
            throw InvalidDateException.eventRangeInverted();
        }

        assert !to.isBefore(from) : "event /to must not be before /from";
        return new AddEventCommand(desc, from, to);
    }

    private static int parseIndex(String cmd, String rest) throws YapGPTException {
        String arg = requireNonEmpty(cmd, rest);
        try {
            int idx = Integer.parseInt(arg.trim());
            return idx;
        } catch (NumberFormatException e) {
            throw new UnknownCommandException(cmd + " " + arg);
        }
    }

    private static Command parseOn(String rest) throws YapGPTException {
        String arg = requireNonEmpty("on", rest);
        try {
            LocalDate d = DateParser.parseFlexibleDateTime(arg).toLocalDate();
            return new OnDateCommand(d);
        } catch (Exception e) {
            throw new InvalidDateException("query", arg);
        }
    }

    private static Command parseFind(String rest) throws EmptyDescriptionException {
        return new FindCommand(requireNonEmpty("find", rest));
    }

    /**
     * Requires a non-empty argument tail for a command; trims surrounding whitespace.
     *
     * @param cmd  Command keyword (used for error messages).
     * @param rest The remainder of the line after the keyword; may be empty.
     * @return A non-empty trimmed string.
     * @throws EmptyDescriptionException If {@code rest} is empty or blank.
     */
    private static String requireNonEmpty(String cmd, String rest) throws EmptyDescriptionException {
        String s = (rest == null) ? "" : rest.trim();
        if (s.isEmpty()) {
            throw new EmptyDescriptionException(cmd);
        }
        return s;
    }
}

