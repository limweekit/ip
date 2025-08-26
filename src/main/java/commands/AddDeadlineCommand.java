package commands;

import app.Ui;
import model.Deadline;
import model.Task;
import model.TaskList;
import storage.Storage;

import java.time.LocalDateTime;

public class AddDeadlineCommand extends Command {
    private final String desc;
    private final LocalDateTime by;
    public AddDeadlineCommand(String desc, LocalDateTime by) { this.desc = desc; this.by = by; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new Deadline(desc, by);
        tasks.add(t);
        ui.boxPrint("Got it! I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
        storage.save(new java.util.ArrayList<>(tasks.asList()));
    }
}
