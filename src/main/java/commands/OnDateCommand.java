package commands;

import app.Ui;
import model.Deadline;
import model.Event;
import model.Task;
import model.TaskList;
import parser.DateParser;
import storage.Storage;

import java.time.LocalDate;

/**
 * Handles the rendering of the list of tasks that occur on the query date.
 */
public class OnDateCommand extends Command {
    private final LocalDate queryDate;

    public OnDateCommand(LocalDate queryDate) {
        this.queryDate = queryDate;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        StringBuilder sb = new StringBuilder("Running internal checks...\n" +
                "Here are the tasks on "
                + queryDate.format(DateParser.OUT_DATE) + ":\n");
        int count = 0;

        for (Task t : tasks.asList()) {
            if (t instanceof Deadline d) {
                if (d.getBy().toLocalDate().equals(queryDate)) {
                    sb.append("- ").append(d).append("\n");
                    count++;
                }
            } else if (t instanceof Event ev) {
                LocalDate from = ev.getFrom().toLocalDate();
                LocalDate to = ev.getTo().toLocalDate();
                if (!queryDate.isBefore(from) && !queryDate.isAfter(to)) {
                    sb.append("- ").append(ev).append("\n");
                    count++;
                }
            }
        }

        if (count == 0) {
            return "Oops! No tasks found on that date.";
        } else {
            return sb.toString();
        }
    }
}
