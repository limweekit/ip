package exceptions;

public class InvalidIndexException extends YapGPTException {
    public InvalidIndexException(String action, int max) {
        super(max == 0
                ? "Uh uh, no item in the list to " + action + "."
                : "Invalid task number to " + action + ". There are only " + max + " items in the list.");
    }
}
