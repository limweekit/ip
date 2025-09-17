package commands;

import app.Ui;
import model.Deadline;
import model.Task;
import model.TaskList;
import storage.Storage;

import java.time.LocalDateTime;

/**
 * Handles adding a Deadline task to the task list.
 */
public class AddDeadlineCommand extends Command {
    private final String desc;
    private final LocalDateTime by;

    public AddDeadlineCommand(String desc, LocalDateTime by) {
        this.desc = desc;
        this.by = by;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new Deadline(desc, by);
        tasks.add(t);
        storage.save(new java.util.ArrayList<>(tasks.asList()));
        String response = "Data received! I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.";

        return response;
    }
}
