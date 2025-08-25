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
        if (desc.isEmpty()) throw new EmptyDescriptionException("deadline");

        String by = (parts.length > 1) ? parts[1].trim() : "unspecified";
        Task t = new Deadline(desc, by);
        tasks.add(t);
        boxPrint("Got it! I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private static void handleEvent(String input, ArrayList<Task> tasks) throws YapGPTException {
        String body = requireArg(input, "event", 5);
        String[] a = body.split("/from", 2);
        String desc = a[0].trim();
        if (desc.isEmpty()) throw new EmptyDescriptionException("event");

        String from = "unspecified", to = "unspecified";
        if (a.length > 1) {
            String[] b = a[1].split("/to", 2);
            from = b[0].trim();
            if (b.length > 1) to = b[1].trim();
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
}


