package commands;

import app.Ui;
import exceptions.InvalidIndexException;
import model.Task;
import model.TaskList;
import storage.Storage;

/**
 * Handles marking a task on the task list based on its index number.
 */
public class MarkCommand extends Command {
    private final int idx;

    public MarkCommand(int idx) {
        this.idx = idx;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws InvalidIndexException {
        int size = tasks.size();
        if (size == 0) {
            throw new InvalidIndexException("mark", 0);
        }
        if (idx < 1 || idx > size) {
            throw new InvalidIndexException("mark", size);
        }
        Task t = tasks.get(idx);
        t.markAsDone();
        storage.save(new java.util.ArrayList<>(tasks.asList()));
        String response = "Nice one! I've marked this task as done:\n  " + t;

        return response;
    }
}
