package commands;

import app.Ui;
import exceptions.InvalidIndexException;
import model.Task;
import model.TaskList;
import storage.Storage;

/**
 * Handles deleting a task from the task list via its index number.
 */
public class DeleteCommand extends Command {
    private final int idx;

    public DeleteCommand(int idx) {
        this.idx = idx;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws InvalidIndexException {
        int size = tasks.size();
        if (size == 0) throw new InvalidIndexException("delete", 0);
        if (idx < 1 || idx > size) throw new InvalidIndexException("delete", size);

        Task removed = tasks.remove(idx);
        ui.boxPrint("Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.");
        storage.save(new java.util.ArrayList<>(tasks.asList()));
    }
}
