package commands;

import app.Ui;
import exceptions.InvalidIndexException;
import model.Task;
import model.TaskList;
import storage.Storage;

public class MarkCommand extends Command {
    private final int idx1;
    public MarkCommand(int idx1) { this.idx1 = idx1; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws InvalidIndexException {
        int size = tasks.size();
        if (size == 0) throw new InvalidIndexException("mark", 0);
        if (idx1 < 1 || idx1 > size) throw new InvalidIndexException("mark", size);

        Task t = tasks.get(idx1);
        t.markAsDone();
        ui.boxPrint("Nice one! I've marked this task as done:\n  " + t);
        storage.save(new java.util.ArrayList<>(tasks.asList()));
    }
}
