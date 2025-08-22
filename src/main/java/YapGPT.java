import java.util.Scanner;

public class YapGPT {

    private static void boxPrint(String message) {
        String divider = "___________________________________________\n"; // 43 underscores
        System.out.println(divider + message + "\n" + divider);
    }

    public static void main(String[] args) {
        final String welcomeMessage =
                "Hello! I'm YapGPT, your favourite chatbot. \n"
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
                    String tasksMessage = "Here are the tasks in your list: \n";
                    StringBuilder sb = new StringBuilder(tasksMessage);
                    for (int i = 0; i < count; i++) {
                        sb.append(i + 1).append(". ").append(tasks[i].toString()).append("\n");
                    }
                    boxPrint(sb.toString());
                }
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

                    int rand = (int)(Math.random() * 2);
                    String string1 = "Productive today are we? ";
                    String string2 = "You're on a roll! ";
                    String doneMessage = rand == 0 ? string1 : string2;

                    boxPrint(doneMessage + "I've marked this task as done:\n  " + t);
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


