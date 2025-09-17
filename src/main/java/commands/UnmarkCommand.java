package commands;

import app.Ui;
import exceptions.InvalidIndexException;
import model.Task;
import model.TaskList;
import storage.Storage;

/**
 * Handles unmarking a task on the task list based on its index number.
 */
public class UnmarkCommand extends Command {
    private final int idx;

    public UnmarkCommand(int idx) {
        this.idx = idx;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws InvalidIndexException {
        int size = tasks.size();
        if (size == 0) {
            throw new InvalidIndexException("unmark", 0);
        }
        if (idx < 1 || idx > size) {
            throw new InvalidIndexException("unmark", size);
        }
        Task t = tasks.get(idx);
        t.markAsUndone();
        storage.save(new java.util.ArrayList<>(tasks.asList()));
        String response = "OK, I've marked this task as not done:\n  "
                + t
                + "\nLet's get to working on it then!";

        return response;
    }
}
