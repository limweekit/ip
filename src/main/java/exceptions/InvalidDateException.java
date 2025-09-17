package exceptions;

/**
 * Thrown when an invalid date is provided.
 * Applicable to deadline, event, on <date>.
 */
public class InvalidDateException extends YapGPTException {
    private static final String HINT =
            "Try formats like: yyyy-MM-dd, yyyy-MM-dd HHmm, d/M/yyyy, "
                    + "d/M/yyyy HHmm, or MMM dd yyyy (e.g., Oct 15 2019).";

    /**
     * General case of bad or missing date/time token for a given context.
     *
     * @param when The context in which the invalid date occurred (e.g. "deadline").
     * @param raw  The raw user input for the date/time.
     */
    public InvalidDateException(String when, String raw) {
        super("Invalid " + when + " date/time: \"" + raw + "\".\n" + HINT);
    }

    /**
     * Specific case where event time range is inverted (to < from).
     *
     * @return An InvalidDateException with the event range message.
     */
    public static InvalidDateException eventRangeInverted() {
        return new InvalidDateException(
                "Oops! Event /from date must be on or before the /to date. Try again!\n"
        );
    }

    // Private constructor for direct message case
    private InvalidDateException(String message) {
        super(message);
    }
}
