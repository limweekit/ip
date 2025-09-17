package commands;

import exceptions.YapGPTException;
import model.Task;
import model.TaskList;
import model.TaskType;
import storage.Storage;
import app.Ui;

import java.util.EnumMap;
import java.util.Map;

/**
 * Handles the displaying of task statistics.
 */
public class StatsCommand extends Command {
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws YapGPTException {

        Map<TaskType, Integer> counts = new EnumMap<>(TaskType.class);
        for (TaskType tt : TaskType.values()) {
            counts.put(tt, 0);
        }

        for (Task t : tasks.asList()) {
            if (t == null) {
                continue;
            }

            TaskType tt = t.getType();

            if (tt != null) {
                counts.put(tt, counts.get(tt) + 1);
            }
        }

        int TODOS = counts.getOrDefault(TaskType.TODO, 0);
        int DEADLINES = counts.getOrDefault(TaskType.DEADLINE, 0);
        int EVENTS = counts.getOrDefault(TaskType.EVENT, 0);
        int TOTAL = TODOS + DEADLINES + EVENTS;

        String response = String.format(
                """
                        Fetching information...
                        Task stats:
                        - ToDo: %d
                        - Deadline: %d
                        - Event: %d
                        Total: %d""",
                TODOS, DEADLINES, EVENTS, TOTAL);

        return response;
    }
}
