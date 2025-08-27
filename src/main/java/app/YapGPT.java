package app;

import commands.Command;
import exceptions.YapGPTException;
import model.TaskList;
import parser.Parser;
import storage.Storage;

public class YapGPT {

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

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

    public static void main(String[] args) {
        new YapGPT("data/yapgpt.txt").run();
    }
}


