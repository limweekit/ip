package commands;

import app.Ui;
import exceptions.YapGPTException;
import model.TaskList;
import storage.Storage;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws YapGPTException;

    public boolean isExit() { return false; }
}
