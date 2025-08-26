package commands;

import app.Ui;
import exceptions.InvalidIndexException;
import model.Task;
import model.TaskList;
import storage.Storage;

public class DeleteCommand extends Command {
    private final int idx1;
    public DeleteCommand(int idx1) { this.idx1 = idx1; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws InvalidIndexException {
        int size = tasks.size();
        if (size == 0) throw new InvalidIndexException("delete", 0);
        if (idx1 < 1 || idx1 > size) throw new InvalidIndexException("delete", size);

        Task removed = tasks.remove(idx1);
        ui.boxPrint("Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.");
        storage.save(new java.util.ArrayList<>(tasks.asList()));
    }
}
