package exceptions;

/**
 * Thrown when a required argument for a command is missing.
 */
public class EmptyDescriptionException extends YapGPTException {
    public EmptyDescriptionException(String cmd) {
        super("The description for '" + cmd + "' cannot be empty.");
    }
}
