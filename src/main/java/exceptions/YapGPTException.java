package exceptions;

/**
 * Checked exceptions for errors while running YapGPT.
 */
public class YapGPTException extends Exception {

    /**
     * Creates an exception with a user-friendly message.
     *
     * @param message The error message to be displayed to user.
     */
    public YapGPTException(String message) {
        super(message);
    }
}
