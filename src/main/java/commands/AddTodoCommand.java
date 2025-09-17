package commands;

import app.Ui;
import model.Task;
import model.TaskList;
import model.ToDo;
import storage.Storage;

/**
 * Handles adding a ToDo task to the task list.
 */
public class AddTodoCommand extends Command {
    private final String desc;

    public AddTodoCommand(String desc) {
        this.desc = desc;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new ToDo(desc);
        tasks.add(t);
        storage.save(new java.util.ArrayList<>(tasks.asList()));
        String response = "Data received! I've added this task:\n  "
                + t
                + "\nNow you have " + tasks.size() + " tasks in the list.";

        return response;

    }
}
