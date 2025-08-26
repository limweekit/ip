package commands;

import app.Ui;
import model.Task;
import model.TaskList;
import model.ToDo;
import storage.Storage;

public class AddTodoCommand extends Command {
    private final String desc;
    public AddTodoCommand(String desc) { this.desc = desc; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new ToDo(desc);
        tasks.add(t);
        ui.boxPrint("Got it! I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.");
        storage.save(new java.util.ArrayList<>(tasks.asList()));
    }
}
