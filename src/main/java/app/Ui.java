package app;

import java.util.Scanner;

public class Ui {
    private final Scanner sc = new Scanner(System.in);
    private static final String DIV = "___________________________________________";

    public void showWelcome() {
        boxPrint("Hello! I'm YapGPT, your favourite chatbot.\n"
                + "What can I do for you?");
    }

    public void showGoodbye() {
        boxPrint("Bye! Hope to see you again soon!");
    }

    public void boxPrint(String message) {
        System.out.println(DIV);
        System.out.println(message);
        System.out.println(DIV);
    }

    public void showError(String message) {
        boxPrint("Uh Oh! " + message);
    }

    public String readCommand() {
        System.out.print("You: ");
        if (!sc.hasNextLine()) {
            return null;
        }
        return sc.nextLine().trim();
    }

    public void close() {
        sc.close();
    }
}
