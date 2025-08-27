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
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
        ui.close();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
