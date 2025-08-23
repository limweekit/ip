package exceptions;

public class ListFullException extends YapGPTException {
    public ListFullException(int capacity) {
        super("Sorry, the list is full! (" + capacity + " items");
    }
}
