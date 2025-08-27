package commands;

import app.Ui;
import exceptions.YapGPTException;
import model.Task;
import model.TaskList;
import storage.Storage;

public class FindCommand extends Command {
    private final String query;

    /**
     * Command that searches task descriptions that match the given query strings (case-insensitive).
     * Multiple words are treated as an AND query (all query words must appear in any order).
     */
    public FindCommand(String query) {
        this.query = query.trim();
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws YapGPTException {
        String[] tokens = query.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        int shown = 0;

        for (int i = 1; i <= tasks.size(); i++) {
            Task t = tasks.get(i);
            String desc = t.getDescription().toLowerCase();

            boolean allMatch = true;
            for (String tkn : tokens) {
                if (!desc.contains(tkn)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                if (shown == 0) {
                    sb.append("Here are the matching tasks in your list:\n");
                } else {
                    sb.append('\n');
                }
                shown++;
                sb.append(shown).append(". ").append(t);
            }
        }

        if (shown == 0) {
            ui.boxPrint("Sorry! No matching tasks found.");
        } else {
            ui.boxPrint(sb.toString());
        }
    }
}

