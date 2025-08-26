package parser;

import commands.*;
import exceptions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Parser {

    public static Command parse(String input) throws YapGPTException {
        if (input == null) {
            return new ExitCommand();
        }
        input = input.trim();
        if (input.equalsIgnoreCase("bye")) {
            return new ExitCommand();
        }
        if (input.equalsIgnoreCase("list")) {
            return new ListCommand();
        }

        if (input.startsWith("todo")) {
            String desc = requireArg(input, "todo", 4);
            return new AddTodoCommand(desc);
        }

        if (input.startsWith("deadline")) {
            String body = requireArg(input, "deadline", 8);
            String[] parts = body.split("/by", 2);
            String desc = parts[0].trim();
            if (desc.isEmpty()) {
                throw new EmptyDescriptionException("deadline");
            }
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new InvalidDateException("deadline", "(missing)");
            }

            String rawBy = parts[1].trim();
            LocalDateTime by;
            try {
                by = DateParser.parseFlexibleDateTime(rawBy);
            }
            catch (IllegalArgumentException ex) {
                throw new InvalidDateException("deadline", rawBy);
            }

            return new AddDeadlineCommand(desc, by);
        }

        if (input.startsWith("event")) {
            String body = requireArg(input, "event", 5);
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

            LocalDateTime from, to;
            try {
                from = DateParser.parseFlexibleDateTime(fromRaw);
            }
            catch (IllegalArgumentException ex) {
                throw new InvalidDateException("event start", fromRaw);
            }
            String toRaw = b[1].trim();
            try {
                to = DateParser.parseFlexibleDateTime(toRaw);
            }
            catch (IllegalArgumentException ex) {
                throw new InvalidDateException("event end", toRaw);
            }

            return new AddEventCommand(desc, from, to);
        }

        if (input.startsWith("mark")) {
            int idx = parseIndexOrUsage(input, "mark", 4);
            return new MarkCommand(idx);
        }

        if (input.startsWith("unmark")) {
            int idx = parseIndexOrUsage(input, "unmark", 6);
            return new UnmarkCommand(idx);
        }

        if (input.startsWith("delete")) {
            int idx = parseIndexOrUsage(input, "delete", 6);
            return new DeleteCommand(idx);
        }

        if (input.startsWith("on")) {
            String arg = requireArg(input, "on", 2);
            LocalDate queryDate;
            try { queryDate = DateParser.parseFlexibleDateTime(arg).toLocalDate(); }
            catch (Exception e) { throw new InvalidDateException("query", arg); }
            return new OnDateCommand(queryDate);
        }

        throw new UnknownCommandException(input);
    }

    private static String requireArg(String input, String cmd, int startIndex) throws EmptyDescriptionException {
        String arg = input.length() > startIndex ? input.substring(startIndex).trim() : "";
        if (arg.isEmpty()) {
            throw new EmptyDescriptionException(cmd);
        }
        return arg;
    }

    private static int parseIndexOrUsage(String input, String cmd, int after) throws YapGPTException {
        String number = input.length() > after ? input.substring(after).trim() : "";
        if (number.isEmpty()) {
            throw new YapGPTException("Here is the proper usage: " + cmd + " <number>");
        }
        try {
            int idx = Integer.parseInt(number);
            if (idx < 1) {
                throw new NumberFormatException();
            }
            return idx;
        } catch (NumberFormatException e) {
            throw new YapGPTException("Here is the proper usage: " + cmd + " <number>");
        }
    }
}
