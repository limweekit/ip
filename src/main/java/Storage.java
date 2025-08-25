import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path dataFile;

    public Storage() {
        this.dataFile = Paths.get("data", "yapgpt.txt");
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            if (Files.notExists(dataFile.getParent())) {
                Files.createDirectories(dataFile.getParent());
            }
            if (Files.notExists(dataFile)) {
                Files.createFile(dataFile);
                return tasks; // empty on first run
            }

            List<String> lines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
            for (String line : lines) {
                Task t = decode(line);
                if (t != null) {
                    tasks.add(t); // skip corrupted lines
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
            if (Files.notExists(dataFile.getParent())) {
                Files.createDirectories(dataFile.getParent());
            }

            Path tmp = Files.createTempFile(dataFile.getParent(), "yapgpt", ".tmp");
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
     * Expected formats for each task type:
     * T | 1 | desc
     * D | 0 | desc | by
     * E | 1 | desc | from | to
     **/
    private Task decode(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        try {
            String[] parts = line.split("\\s*\\|\\s*");
            String type = parts[0];
            int done = Integer.parseInt(parts[1]);
            String desc = parts[2];

            Task t;
            switch (type) {
                case "T" -> t = new ToDo(desc);
                case "D" -> {
                    String by = parts.length > 3 ? parts[3] : "unspecified";
                    t = new Deadline(desc, by);
                }
                case "E" -> {
                    String from = parts.length > 3 ? parts[3] : "unspecified";
                    String to = parts.length > 4 ? parts[4] : "unspecified";
                    t = new Event(desc, from, to);
                }
                default -> { return null; } // corrupted or unknown type
            }
            if (done == 1) t.markAsDone();
            return t;
        } catch (Exception e) {
            return null; // skip corrupted lines
        }
    }
}
