package storage;

import model.Deadline;
import model.Event;
import model.Task;
import model.ToDo;
import parser.DateParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistence gateway for YapGPT tasks.
 * Stores in ./data/yapgpt.txt.
 */
public class Storage {
    private final Path dataFile;

    public Storage() {
        this(Paths.get("data", "yapgpt.txt").toString());
    }

    public Storage(String filePath) {
        assert filePath != null && !filePath.isBlank()
                : "Storage filePath must be non-null";
        this.dataFile = Paths.get(filePath);
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            Path dir = dataFile.getParent();
            if (dir != null && Files.notExists(dir)) {
                Files.createDirectories(dir);
                assert Files.exists(dir) : "Storage directory should exist after creation";
            }
            if (Files.notExists(dataFile)) {
                Files.createFile(dataFile);
                assert Files.exists(dataFile) : "Storage data file should exist after creation";
                return tasks; // empty on first run
            }

            List<String> lines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
            for (String line : lines) {
                Task t = decode(line);
                if (t != null) {
                    tasks.add(t);
                }
            }
        } catch (IOException ignored) {
            // If IO fails, return empty list
        }
        return tasks;
    }

    public void save(ArrayList<Task> tasks) {
        ArrayList<String> lines = new ArrayList<>(tasks.size());
        for (Task t : tasks) {
            lines.add(t.serialize());
        }
        try {
            Path dir = dataFile.getParent();
            if (dir != null && Files.notExists(dir)) {
                Files.createDirectories(dir);
            }

            Path tmp = Files.createTempFile(dir != null ? dir : Paths.get("."), "yapgpt", ".tmp");
            Files.write(tmp, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);

            try {
                Files.move(tmp, dataFile,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, dataFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ignored) {}
    }

    /**
     * Decode a line from file into a Task.
     * Expected format:
     * T | 1 | desc
     * D | 0 | desc | by
     * E | 1 | desc | from | to
     */
    private Task decode(String line) {
        if (line == null) {
            return null;
        }
        line = line.replace("\uFEFF", "").trim();
        if (line.isEmpty()) {
            return null;
        }
        if (line.startsWith("#") || line.startsWith("//")) {
            return null;
        }
        String[] parts = line.split("\\s*\\|\\s*", 5);
        if (parts.length < 3) {
            return null;
        }
        try {
            String type = parts[0].trim();
            int done = Integer.parseInt(parts[1].trim());
            String desc = parts[2];

            assert desc != null : "Description must not be null";
            assert done == 0 || done == 1 : "Done must be 0 or 1";
          
            Task t = switch (type) {
                case "T" -> new ToDo(desc);
                case "D" -> parseDeadline(desc, parts);
                case "E" -> parseEvent(desc, parts);
                default -> null;
            };
          
            if (done == 1) {
                assert t != null : "Task is null";
                t.markAsDone();
            }
            return t;
        } catch (Exception e) {
            return null; // skip corrupted lines
        }
    }

    /**
     * Parses a serialized line into a {@link Deadline} task.
     *
     * @param desc The description of the deadline task.
     * @param parts The full split line tokens from serialized file.
     * @return A {@link Deadline} task if parsing is successful, null otherwise.
     */
    private Deadline parseDeadline(String desc, String[] parts) {
        if (parts.length < 4) {
            return null;
        }
        String rawBy = stripTrailingComment(parts[3]).trim();
        if (rawBy.isEmpty()) {
            return null;
        }
        LocalDateTime by = DateParser.parseFlexibleDateTime(rawBy);
        return new Deadline(desc, by);
    }

    /**
     * Parses a serialized task into a {@link Event} task.
     *
     * @param desc The description of the event class.
     * @param parts The full split line tokens from serialized file.
     * @return A {@link Event} task if parsing is successful, null otherwise.
     */
    private Event parseEvent(String desc, String[] parts) {
        if (parts.length < 5) {
            return null;
        }
        String rawFrom = stripTrailingComment(parts[3]).trim();
        String rawTo = stripTrailingComment(parts[4]).trim();
        if (rawFrom.isEmpty() || rawTo.isEmpty()) {
            return null;
        }
        LocalDateTime from = DateParser.parseFlexibleDateTime(rawFrom);
        LocalDateTime to = DateParser.parseFlexibleDateTime(rawTo);
        if (to.isBefore(from)) {
            return null;
        }
        return new Event(desc, from, to);
    }

    /**
     * Removes any trailing inline comments from input.
     * Recognises {@code //} and {@code #} as comment delimiters.
     *
     * @param s Token to process.
     * @return The token with any trailing comments removed.
     */
    private static String stripTrailingComment(String s) {
        if (s == null) {
            return null;
        }
        int i = s.indexOf("//");
        if (i >= 0) {
            return s.substring(0, i);
        }
        i = s.indexOf('#');
        if (i >= 0) {
            return s.substring(0, i);
        }
        return s;
    }
}
