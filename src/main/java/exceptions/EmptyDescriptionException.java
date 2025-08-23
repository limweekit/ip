package exceptions;

public class EmptyDescriptionException extends YapGPTException {
    public EmptyDescriptionException(String cmd) {
        super("The description for '" + cmd + "' cannot be empty.");
    }
}
