package commands;

import app.Ui;
import model.TaskList;
import storage.Storage;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.boxPrint(tasks.renderList());
    }
}
