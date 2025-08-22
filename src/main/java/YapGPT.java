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

        String[] list = new String[100];
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
                    boxPrint("No items added yet.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < count; i++) {
                        sb.append(i + 1).append(": ").append(list[i]).append("\n");
                    }
                    boxPrint(sb.toString());
                }
                continue;
            }

            if (count >= list.length) {
                boxPrint("Sorry, list is full.");
            } else {
                list[count++] = input;
                boxPrint("Added: " + input);
            }
        }
        sc.close();
    }
}

