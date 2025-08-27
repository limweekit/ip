package model;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of tasks, utilizing the ArrayList implementation.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int idx) {
        return tasks.get(idx - 1);
    }

    public void add(Task t) {
        tasks.add(t);
    }

    public Task remove(int idx1) {
        return tasks.remove(idx1 - 1);
    }

    public List<Task> asList() {
        return tasks;
    }

    /**
     * Handles the formatting of the list message.
     *
     * @return List of current tasks.
     */
    public String renderList() {
        if (tasks.isEmpty()) {
            return "No tasks added yet.";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
