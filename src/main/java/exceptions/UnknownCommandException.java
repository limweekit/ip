package exceptions;

/**
 * Thrown when the user inputs a command that is not recognised by YapGPT.
 * Displays an error message with list of correct commands and their usages.
 */
public class UnknownCommandException extends YapGPTException {
    public UnknownCommandException(String input) {
        super("I don't recognise \"" + input + "\". \n"
                + "My commands are: todo <desc> | deadline <desc> /by <date> | event <desc> /from <date> /to <date> |\n"
                + "list | mark <number> | unmark <number> | delete <number> | on <date> | bye");
    }
}
