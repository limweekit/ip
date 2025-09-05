package commands;

import app.Ui;
import model.Event;
import model.Task;
import model.TaskList;
import storage.Storage;

import java.time.LocalDateTime;

/**
 * Handles adding an Event task to the task list.
 */
public class AddEventCommand extends Command {
    private final String desc;
    private final LocalDateTime from, to;

    public AddEventCommand(String desc, LocalDateTime from, LocalDateTime to) {
        this.desc = desc;
        this.from = from;
        this.to = to;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new Event(desc, from, to);
        tasks.add(t);
        storage.save(new java.util.ArrayList<>(tasks.asList()));
        String response = "Got it! I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.";

        return response;
    }
}
