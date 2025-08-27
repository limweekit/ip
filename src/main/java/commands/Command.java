package commands;

import app.Ui;
import exceptions.YapGPTException;
import model.TaskList;
import storage.Storage;

/**
 * The abstract superclass of all commands that YapGPT recognises.
 * Handles mutations to the {@link TaskList}, user-visible outputs via {@link Ui},
 * and persistence via {@link Storage}.
 */
public abstract class Command {

    /**
     * Executes the command with its corresponding implementations.
     *
     * @param tasks The task list to operate on.
     * @param ui The UI used for output.
     * @param storage The storage for data persistence.
     * @throws YapGPTException If the command cannot be executed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws YapGPTException;

    /**
     * Indicates whether running this command should terminate the programme.
     *
     * @return true if the app should exit after the command; false otherwise.
     */
    public boolean isExit() { return false; }
}
