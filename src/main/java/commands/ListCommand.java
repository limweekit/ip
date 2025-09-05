package commands;

import app.Ui;
import model.TaskList;
import storage.Storage;

/**
 * Handles rendering the current list of tasks.
 */
public class ListCommand extends Command {
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return tasks.renderList();
    }
}
