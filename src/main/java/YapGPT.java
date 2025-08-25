import java.util.ArrayList;
import java.util.Scanner;
import exceptions.*;

public class YapGPT {

    private static void boxPrint(String message) {
        String divider = "___________________________________________";
        System.out.println(divider);
        System.out.println(message);
        System.out.println(divider);
    }

    private static void boxError(String message) {
        boxPrint("Uh Oh! " + message);
    }

    public static void main(String[] args) {
        final String welcomeMessage =
                "Hello! I'm YapGPT, your favourite chatbot.\n"
                        + "What can I do for you?";

        final String goodbyeMessage = "Bye! Hope to see you again soon!";

        Storage storage = new Storage();
        ArrayList<Task> tasks = storage.load();
        Scanner sc = new Scanner(System.in);

        boxPrint(welcomeMessage);

        while (true) {
            System.out.print("You: ");
            if (!sc.hasNextLine()) {
                boxPrint(goodbyeMessage);
                break;
            }

            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                boxPrint(goodbyeMessage);
                break;
            }

            try {
                if (input.equalsIgnoreCase("list")) {
                    handleList(tasks);
                    continue;
                }
                if (input.startsWith("todo")) {
                    handleTodo(input, tasks);
                    storage.save(tasks);
                    continue;
                }
                if (input.startsWith("deadline")) {
                    handleDeadline(input, tasks);
                    storage.save(tasks);
                    continue;
                }
                if (input.startsWith("event")) {
                    handleEvent(input, tasks);
                    storage.save(tasks);
                    continue;
                }
                if (input.startsWith("mark")) {
                    handleMark(input, tasks);
                    storage.save(tasks);
                    continue;
                }
                if (input.startsWith("unmark")) {
                    handleUnmark(input, tasks);
                    storage.save(tasks);
                    continue;
                }
                if (input.startsWith("delete")) {
                    handleDelete(input, tasks);
                    storage.save(tasks);
                    continue;
                }

                if (input.startsWith("on")) {
                    handleOn(input, tasks);
                    continue;
                }

                // If none matched, it means unknown command or error class I haven't created
                throw new UnknownCommandException(input);

            } catch (YapGPTException e) {
                boxError(e.getMessage());
            } catch (Exception e) {
                boxError("Something went wrong: " + e.getMessage());
            }
        }
        sc.close();
    }

    // Handlers

    private static void handleList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            boxPrint("No tasks added yet.");
            return;
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        boxPrint(sb.toString());
    }

    private static String requireArg(String input, String cmd, int startIndex) throws EmptyDescriptionException {
        String arg = input.length() > startIndex ? input.substring(startIndex).trim() : "";
        if (arg.isEmpty()) throw new EmptyDescriptionException(cmd);
        return arg;
    }

    private static void handleTodo(String input, ArrayList<Task> tasks) throws YapGPTException {
        String desc = requireArg(input, "todo", 4);
        Task t = new ToDo(desc);
        tasks.add(t);
        boxPrint("Got it! I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private static void handleDeadline(String input, ArrayList<Task> tasks) throws YapGPTException {
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
        java.time.LocalDateTime by;

        try {
            by = DateParser.parseFlexibleDateTime(rawBy);
        } catch (IllegalArgumentException ex) {
            throw new InvalidDateException("deadline", rawBy);
        }

        Task t = new Deadline(desc, by);
        tasks.add(t);

        boxPrint("Got it! I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private static void handleEvent(String input, ArrayList<Task> tasks) throws YapGPTException {
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

        String toRaw = b[1].trim();
        java.time.LocalDateTime from, to;

        try {
            from = DateParser.parseFlexibleDateTime(fromRaw);
        } catch (IllegalArgumentException ex) {
            throw new InvalidDateException("event start", fromRaw);
        }
        try {
            to = DateParser.parseFlexibleDateTime(toRaw);
        } catch (IllegalArgumentException ex) {
            throw new InvalidDateException("event end", toRaw);
        }

        Task t = new Event(desc, from, to);
        tasks.add(t);

        boxPrint("Got it! I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private static int parseIndexOrUsage(String input, String cmd, int after, int size) throws YapGPTException {
        if (size == 0) throw new InvalidIndexException(cmd, 0);
        String number = input.length() > after ? input.substring(after).trim() : "";
        if (number.isEmpty()) throw new YapGPTException("Here is the proper usage: " + cmd + " <number>");
        int idx;
        try {
            idx = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new YapGPTException("Here is proper usage: " + cmd + " <number>");
        }
        if (idx < 1 || idx > size) throw new InvalidIndexException(cmd, size);
        return idx;
    }

    private static void handleMark(String input, ArrayList<Task> tasks) throws YapGPTException {
        int idx = parseIndexOrUsage(input, "mark", 4, tasks.size());
        Task t = tasks.get(idx - 1);
        t.markAsDone();
        boxPrint("Nice one! I've marked this task as done:\n  " + t);
    }

    private static void handleUnmark(String input, ArrayList<Task> tasks) throws YapGPTException {
        int idx = parseIndexOrUsage(input, "unmark", 6, tasks.size());
        Task t = tasks.get(idx - 1);
        t.markAsUndone();
        boxPrint("OK, I've marked this task as not done yet:\n  " + t);
    }

    private static void handleDelete(String input, ArrayList<Task> tasks) throws YapGPTException {
        int idx = parseIndexOrUsage(input, "delete", 6, tasks.size());
        Task removed = tasks.remove(idx - 1);
        boxPrint("Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private static void handleOn(String input, ArrayList<Task> tasks) throws YapGPTException {
        String arg = requireArg(input, "on", 2);
        java.time.LocalDate queryDate;
        try {
            queryDate = DateParser.parseFlexibleDateTime(arg).toLocalDate();
        } catch (Exception e) {
            throw new InvalidDateException("query", arg);
        }

        StringBuilder sb = new StringBuilder("Here are the tasks on "
                + queryDate.format(DateParser.OUT_DATE) + ":\n");

        int count = 0;
        for (Task t : tasks) {
            if (t instanceof Deadline d) {
                if (d.getBy().toLocalDate().equals(queryDate)) {
                    sb.append("- ").append(d).append("\n");
                    count++;
                }
            } else if (t instanceof Event ev) {
                java.time.LocalDate from = ev.getFrom().toLocalDate();
                java.time.LocalDate to   = ev.getTo().toLocalDate();
                if (!queryDate.isBefore(from) && !queryDate.isAfter(to)) {
                    sb.append("- ").append(ev).append("\n");
                    count++;
                }
            }
        }
        if (count == 0) {
            boxPrint("No tasks found on that date.");
        } else {
            boxPrint(sb.toString());
        }
    }

}


