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

        Task[] tasks = new Task[100];
        int count = 0;
        Scanner sc = new Scanner(System.in);

        boxPrint(welcomeMessage);

        while (true) {
            System.out.print("You: ");
            if (!sc.hasNextLine()) {
                boxPrint(goodbyeMessage);
                break;
            }

            String input = sc.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                boxPrint(goodbyeMessage);
                break;
            }

            try {
                if (input.equalsIgnoreCase("list")) {
                    handleList(tasks, count);
                    continue;
                }
                if (input.startsWith("todo")) {
                    handleTodo(input, tasks, count);
                    count++;
                    continue;
                }
                if (input.startsWith("deadline")) {
                    handleDeadline(input, tasks, count);
                    count++;
                    continue;
                }
                if (input.startsWith("event")) {
                    handleEvent(input, tasks, count);
                    count++;
                    continue;
                }
                if (input.startsWith("mark")) {
                    handleMark(input, tasks, count);
                    continue;
                }
                if (input.startsWith("unmark")) {
                    handleUnmark(input, tasks, count);
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
    private static void handleList(Task[] tasks, int count) {
        if (count == 0) {
            boxPrint("No tasks added yet.");
            return;
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < count; i++) {
            sb.append(i + 1).append(". ").append(tasks[i]).append("\n");
        }
        boxPrint(sb.toString());
    }

    private static void ensureCapacity(Task[] tasks, int count) throws ListFullException {
        if (count >= tasks.length) throw new ListFullException(tasks.length);
    }

    private static String requireArg(String input, String cmd, int startIndex) throws EmptyDescriptionException {
        String arg = input.length() > startIndex ? input.substring(startIndex).trim() : "";
        if (arg.isEmpty()) throw new EmptyDescriptionException(cmd);
        return arg;
    }

    private static void handleTodo(String input, Task[] tasks, int count) throws YapGPTException {
        ensureCapacity(tasks, count);
        String desc = requireArg(input, "todo", 4);
        tasks[count] = new ToDo(desc);
        boxPrint("Got it! I've added this task:\n  " + tasks[count]
                + "\nNow you have " + (count + 1) + " tasks in the list.");
    }

    private static void handleDeadline(String input, Task[] tasks, int count) throws YapGPTException {
        ensureCapacity(tasks, count);
        String body = requireArg(input, "deadline", 8);
        String[] parts = body.split("/by", 2);
        String desc = parts[0].trim();

        if (desc.isEmpty()) throw new EmptyDescriptionException("deadline");

        String by = (parts.length > 1) ? parts[1].trim() : "unspecified";
        tasks[count] = new Deadline(desc, by);
        boxPrint("Got it! I've added this task:\n  " + tasks[count]
                + "\nNow you have " + (count + 1) + " tasks in the list.");
    }

    private static void handleEvent(String input, Task[] tasks, int count) throws YapGPTException {
        ensureCapacity(tasks, count);
        String body = requireArg(input, "event", 5);
        String[] a = body.split("/from", 2);
        String desc = a[0].trim();
        if (desc.isEmpty())
            throw new EmptyDescriptionException("event");
        String from = "unspecified", to = "unspecified";
        if (a.length > 1) {
            String[] b = a[1].split("/to", 2);
            from = b[0].trim();
            if (b.length > 1)
                to = b[1].trim();
        }
        tasks[count] = new Event(desc, from, to);
        boxPrint("Got it! I've added this task:\n  " + tasks[count]
                + "\nNow you have " + (count + 1) + " tasks in the list.");
    }

    private static int parseIndexOrThrow(String input, String keyword, int after) throws YapGPTException {
        String number = input.length() > after ? input.substring(after).trim() : "";
        if (number.isEmpty()) throw new UnknownCommandException(input);
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new UnknownCommandException(input);
        }
    }

    private static void handleMark(String input, Task[] tasks, int count) throws YapGPTException {
        if (count == 0) throw new InvalidIndexException("mark", 0);
        int idx = parseIndexOrThrow(input, "mark", 4);
        if (idx < 1 || idx > count) throw new InvalidIndexException("mark", count);
        Task t = tasks[idx - 1];
        t.markAsDone();
        boxPrint("Nice one! I've marked this task as done:\n  " + t);
    }

    private static void handleUnmark(String input, Task[] tasks, int count) throws YapGPTException {
        if (count == 0) throw new InvalidIndexException("unmark", 0);
        int idx = parseIndexOrThrow(input, "unmark", 6);
        if (idx < 1 || idx > count) throw new InvalidIndexException("unmark", count);
        Task t = tasks[idx - 1];
        t.markAsUndone();
        boxPrint("OK, I've marked this task as not done yet:\n  " + t);
    }
}
