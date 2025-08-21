import java.util.Scanner;

public class YapGPT {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String divider = "___________________________________________\n"; // 43 underscores
        String welcomeMessage =
                  divider
                + "Hello! I'm YapGPT, your favourite chatbot. \n"
                + "What can I do for you? \n"
                + divider;

        String goodbyeMessage =
                divider
                + "Bye! Hope to see you again soon! \n"
                + divider;

        System.out.println(welcomeMessage);

        while (true) {
            // Small customization to make the chatbot act like a messaging app
            System.out.print("You: ");
            String input = sc.nextLine();

            if (input.equals("bye")) {
                System.out.println(goodbyeMessage);
                break;
            }
            System.out.println("YapGPT: " + input);
        }
        sc.close();
    }
}

