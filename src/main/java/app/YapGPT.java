package app;

import commands.Command;
import exceptions.YapGPTException;
import model.TaskList;
import parser.Parser;
import storage.Storage;

/**
 * Entry point and main application of YapGPT.
 * Responsible for wiring together the UI, storage, and in-memory task list,
 * then running the read–parse–execute cycle until an exit command is issued.
 */
public class YapGPT {

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;


    /**
     * Creates a YapGPT application bound to the given storage file.
     * Attempts to load existing tasks from storage; on failure, start with an empty list.
     *
     * @param filePath Path to the persistent tasks file (data/yapgpt.txt).
     */
    public YapGPT(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);

        try {
            this.tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Failed to load tasks.");
            this.tasks = new TaskList();
        }
    }

    /**
     * Runs the main application loop.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String fullCommand = ui.readCommand();
            try {
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (YapGPTException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("Something went wrong: " + e.getMessage());
            }
        }
    }

    /**
     * Application entry point.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new YapGPT("data/yapgpt.txt").run();
    }
}


