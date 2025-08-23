package exceptions;

public class UnknownCommandException extends YapGPTException {
    public UnknownCommandException(String input) {
        super("I don't recognise \"" + input + "\". \n"
                + "My commands are: todo <desc> | deadline <desc> /by <date> | event <desc> /from <date> /to <date>"
                + "| list | mark <number> | unmark <number> | bye");
    }
}
