import java.util.Scanner;

public class YapGPT {

    private static void boxPrint(String message) {
        String divider = "___________________________________________";
        System.out.println(divider);
        System.out.println(message.trim());
        System.out.println(divider);
    }

    public static void main(String[] args) {
        final String welcomeMessage =
                "Hello! I'm YapGPT, your favourite chatbot.\n"
                        + "What can I do for you?";

        final String goodbyeMessage =
                "Bye! Hope to see you again soon!";

        Task[] tasks = new Task[100];
        int count = 0;
        Scanner sc = new Scanner(System.in);

        boxPrint(welcomeMessage);

        while (true) {
            // Small customization to make the chatbot act like a messaging app
            System.out.print("You: ");
            if (!sc.hasNextLine()) {
                boxPrint(goodbyeMessage);
                break;
            }

            String input = sc.nextLine();

            if (input.equals("bye")) {
                boxPrint(goodbyeMessage);
                break;
            }

            if (input.equals("list")) {
                if (count == 0) {
                    boxPrint("No tasks added yet.");
                } else {
                    String tasksMessage = "Here are the tasks in your list:\n";
                    StringBuilder sb = new StringBuilder(tasksMessage);
                    for (int i = 0; i < count; i++) {
                        sb.append(i + 1).append(". ").append(tasks[i].toString()).append("\n");
                    }
                    boxPrint(sb.toString());
                }
                continue;
            }

            if (input.startsWith("todo ")) {
                String desc = input.substring(5);
                if (desc.isEmpty()) {
                    boxPrint("Usage: todo <description>");
                    continue;
                }
                if (count >= tasks.length) {
                    boxPrint("Sorry, the tasks list is full.");
                    continue;
                }
                Task t = new ToDo(desc);
                tasks[count++] = t;
                boxPrint("Got it! I've added this task:\n  " + t
                        + "\nNow you have " + count + " tasks in the list.");
                continue;
            }

            if (input.startsWith("deadline ")) {
                String body = input.substring(9);
                String desc, by;
                String[] parts = body.split(" /by ", 2);
                desc = parts[0];
                by = (parts.length > 1) ? parts[1] : "unspecified";
                if (desc.isEmpty()) {
                    boxPrint("Usage: deadline <description> /by <when>");
                    continue;
                }
                if (count >= tasks.length) {
                    boxPrint("Sorry, the tasks list is full.");
                    continue;
                }
                Task t = new Deadline(desc, by);
                tasks[count++] = t;
                boxPrint("Got it! I've added this task:\n  " + t
                        + "\nNow you have " + count + " tasks in the list.");
                continue;
            }

            if (input.startsWith("event ")) {
                String body = input.substring(6);
                String desc, from = "unspecified", to = "unspecified";

                String[] a = body.split(" /from ", 2);
                desc = a[0];
                if (a.length > 1) {
                    String[] b = a[1].split(" /to ", 2);
                    from = b[0];
                    if (b.length > 1) {
                        to = b[1];
                    }
                }

                if (desc.isEmpty()) {
                    boxPrint("Usage: event <description> /from <from> /to <to>");
                    continue;
                }
                if (count >= tasks.length) {
                    boxPrint("Sorry, the tasks list is full.");
                    continue;
                }
                Task t = new Event(desc, from, to);
                tasks[count++] = t;
                boxPrint("Got it! I've added this task:\n  " + t
                        + "\nNow you have " + count + " tasks in the list.");
                continue;
            }

            if (input.startsWith("mark ")) {
                try {
                    int idx = Integer.parseInt(input.substring(5));
                    if (idx < 1 || idx > count) {
                        boxPrint("Invalid task number.");
                        continue;
                    }
                    Task t = tasks[idx - 1];
                    t.markAsDone();

                    boxPrint("Nice! I've marked this task as done:\n  " + t);
                } catch (NumberFormatException e) {
                    boxPrint("Please provide a valid number.");
                }
                continue;
            }

            if (input.startsWith("unmark ")) {
                try {
                    int idx = Integer.parseInt(input.substring(7));
                    if (idx < 1 || idx > count) {
                        boxPrint("Invalid task number.");
                        continue;
                    }
                    Task t = tasks[idx - 1];
                    t.markAsUndone();
                    boxPrint("OK, I've marked this task as incomplete:\n  " + t);
                } catch (NumberFormatException e) {
                    boxPrint("Please provide a valid number.");
                }
                continue;
            }

            if (count >= tasks.length) {
                boxPrint("Sorry, the tasks list is full.");
            } else {
                tasks[count++] = new Task(input);
                boxPrint("Added: " + input);
            }
        }
        sc.close();
    }
}


