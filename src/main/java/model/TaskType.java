package model;

/**
 * Enumerates the kinds of tasks supported by YapGPT.
 * Each enum carries a short symbol used for display and on-disk serialization.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    TaskType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
