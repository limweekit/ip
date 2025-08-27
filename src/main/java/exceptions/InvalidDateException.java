package exceptions;

/**
 * Thrown when an invalid date is provided.
 * Applicable to deadline, event, on <date>.
 */
public class InvalidDateException extends YapGPTException {
    public InvalidDateException(String when, String raw) {
        super("Invalid " + when + " date/time: \"" + raw + "\".\n"
                + "Try formats like: yyyy-MM-dd, yyyy-MM-dd HHmm, d/M/yyyy, d/M/yyyy HHmm, or MMM dd yyyy (e.g., Oct 15 2019).");
    }
}

