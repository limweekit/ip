package commands;

import app.Ui;
import exceptions.InvalidIndexException;
import model.Task;
import model.TaskList;
import storage.Storage;

public class UnmarkCommand extends Command {
    private final int idx1;
    public UnmarkCommand(int idx1) { this.idx1 = idx1; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws InvalidIndexException {
        int size = tasks.size();
        if (size == 0) throw new InvalidIndexException("unmark", 0);
        if (idx1 < 1 || idx1 > size) throw new InvalidIndexException("unmark", size);

        Task t = tasks.get(idx1);
        t.markAsUndone();
        ui.boxPrint("OK, I've marked this task as not done yet:\n  " + t);
        storage.save(new java.util.ArrayList<>(tasks.asList()));
    }
}
