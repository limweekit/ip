package commands;

import app.Ui;
import model.TaskList;
import storage.Storage;

/**
 * Handles exiting the application loop and ending the programme.
 * Displays goodbye message to user.
 */
public class ExitCommand extends Command {
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return "Bye! Hope to see you again soon! Powering down...";
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
