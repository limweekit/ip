package exceptions;

/**
 * Thrown when an invalid index is provided.
 * Applicable to mark, unmark, delete.
 */
public class InvalidIndexException extends YapGPTException {
    public InvalidIndexException(String action, int max) {
        super(max == 0
                ? "No item in the list to " + action + "."
                : "Invalid task number to " + action + ". Valid range: 1 to " + max + ".");
    }
}
