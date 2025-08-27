package app;

import java.util.Scanner;

public class Ui {
    private final Scanner sc = new Scanner(System.in);
    private static final String divider = "___________________________________________";

    /**
     * Prints the welcome message upon initially running the programme.
     */
    public void showWelcome() {
        boxPrint("Hello! I'm YapGPT, your favourite chatbot.\n"
                + "What can I do for you?");
    }

    /**
     * Prints the goodbye message upon exiting the programme.
     */
    public void showGoodbye() {
        boxPrint("Bye! Hope to see you again soon!");
    }

    /**
     * Prints the input message in a more visually appealing format.
     *
     * @param message The message to be printed.
     */
    public void boxPrint(String message) {
        System.out.println(divider);
        System.out.println(message);
        System.out.println(divider);
    }

    /**
     * Prints the corresponding error message when programme runs into an exception.
     *
     * @param message The error message to be printed.
     */
    public void showError(String message) {
        boxPrint("Uh Oh! " + message);
    }

    /**
     * Reads the user input.
     *
     * @return The user input.
     */
    public String readCommand() {
        System.out.print("You: ");
        if (!sc.hasNextLine()) return null;
        return sc.nextLine().trim();
    }

    /**
     * Closes the programme.
     */
    public void close() {
        sc.close();
    }
}
