package commands;

import app.Ui;
import exceptions.InvalidIndexException;
import exceptions.YapGPTException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import storage.Storage;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CommandsTest {
    static class TestUi extends Ui {
        final ArrayList<String> boxes = new ArrayList<>();
        final ArrayList<String> errors = new ArrayList<>();
        boolean goodbyeShown = false;
        boolean isClosed = false;

        @Override
        public void boxPrint(String message) {
            boxes.add(message);
        }

        @Override
        public void showError(String message) {
            errors.add(message);
        }

        @Override
        public void showGoodbye() {
            goodbyeShown = true;
        }

        @Override
        public void close() {
            isClosed = true;
        }

        String lastBox() {
            return boxes.isEmpty() ? "" : boxes.get(boxes.size() - 1);
        }
    }

    private String storagePath() {
        return tmp.resolve("yapgpt.txt").toString();
    }

    @TempDir
    Path tmp;
    private TestUi ui;
    private Storage storage;
    private TaskList tasks;

    @BeforeEach
    void setUp() {
        ui = new TestUi();
        storage = new Storage(tmp.resolve("yapgpt.txt").toString());
        tasks = new TaskList();
    }

    @Test
    void TodoDeadlineEventList_validInputs_success() {
        // add todo
        new AddTodoCommand("testTodo").execute(tasks, ui, storage);
        assertEquals(1, tasks.size());
        assertInstanceOf(ToDo.class, tasks.get(1));

        // add deadline
        LocalDateTime by = LocalDateTime.of(2025, 12, 1, 23, 59);
        new AddDeadlineCommand("testDeadline", by).execute(tasks, ui, storage);
        assertEquals(2, tasks.size());
        assertInstanceOf(Deadline.class, tasks.get(2));

        // add event
        LocalDateTime from = LocalDateTime.of(2025, 12, 5, 9, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 12, 7, 17, 0);
        new AddEventCommand("testEvent", from, to).execute(tasks, ui, storage);
        assertEquals(3, tasks.size());
        assertInstanceOf(Event.class, tasks.get(3));

        // render list of tasks
        new ListCommand().execute(tasks, ui, storage);
        String out = ui.lastBox();
        assertTrue(out.contains("Here are the tasks in your list"));
        assertTrue(out.contains("testTodo"));
        assertTrue(out.contains("testDeadline"));
        assertTrue(out.contains("testEvent"));

        // verify storage
        var reloaded = new Storage(storagePath()).load();
        assertEquals(3, reloaded.size());
    }

    @Test
    void list_emptyList_showsNoTasksMessage() {
        new ListCommand().execute(tasks, ui, storage);
        assertEquals("No tasks added yet.", ui.lastBox());
    }

    @Test
    void markUnmarkDelete_validInputs_success() throws YapGPTException {
        // seed 2 todos
        new AddTodoCommand("t1").execute(tasks, ui, storage);
        new AddTodoCommand("t2").execute(tasks, ui, storage);

        // mark index 2
        new MarkCommand(2).execute(tasks, ui, storage);
        assertTrue(tasks.get(2).toString().contains("[X]"));

        // unmark index 2
        new UnmarkCommand(2).execute(tasks, ui, storage);
        assertFalse(tasks.get(2).toString().contains("[X]"));

        // delete index 1
        new DeleteCommand(1).execute(tasks, ui, storage);
        assertEquals(1, tasks.size());
        assertTrue(tasks.get(1).toString().contains("t2"));
    }

    @Test
    void markUnmarkDelete_invalidInputs_throwsInvalidIndexException() {
        new AddTodoCommand("t1").execute(tasks, ui, storage);
        int before = tasks.size();

        // Index too large
        assertThrows(InvalidIndexException.class, () ->
                new MarkCommand(2).execute(tasks, ui, storage));
        assertThrows(InvalidIndexException.class, () ->
                new UnmarkCommand(2).execute(tasks, ui, storage));
        assertThrows(InvalidIndexException.class, () ->
                new DeleteCommand(2).execute(tasks, ui, storage));

        // Index 0
        assertThrows(InvalidIndexException.class, () ->
                new MarkCommand(0).execute(tasks, ui, storage));
        assertThrows(InvalidIndexException.class, () ->
                new UnmarkCommand(0).execute(tasks, ui, storage));
        assertThrows(InvalidIndexException.class, () ->
                new DeleteCommand(0).execute(tasks, ui, storage));

        // Negative indexes
        assertThrows(InvalidIndexException.class, () ->
                new MarkCommand(-1).execute(tasks, ui, storage));
        assertThrows(InvalidIndexException.class, () ->
                new UnmarkCommand(-1).execute(tasks, ui, storage));
        assertThrows(InvalidIndexException.class, () ->
                new DeleteCommand(-1).execute(tasks, ui, storage));

        // Check for mutations
        assertEquals(before, tasks.size());
        assertInstanceOf(ToDo.class, tasks.get(1));
        assertFalse(tasks.get(1).toString().contains("[X]"));
    }

    @Test
    void markUnmarkDelete_emptyList_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, () ->
                new MarkCommand(1).execute(tasks, ui, storage));
        assertThrows(InvalidIndexException.class, () ->
                new UnmarkCommand(1).execute(tasks, ui, storage));
        assertThrows(InvalidIndexException.class, () ->
                new DeleteCommand(1).execute(tasks, ui, storage));
        assertEquals(0, tasks.size());
    }

    @Test
    void delete_tillLastItem_showsEmptyList() throws InvalidIndexException {
        new AddTodoCommand("test").execute(tasks, ui, storage);
        new DeleteCommand(1).execute(tasks, ui, storage);
        new ListCommand().execute(tasks, ui, storage);
        assertEquals("No tasks added yet.", ui.lastBox());
    }

    @Test
    void onDate_validInputs_success() {
        // deadline on 10th
        LocalDateTime by = LocalDateTime.of(2025, 10, 10, 9, 0);
        new AddDeadlineCommand("testDeadline", by).execute(tasks, ui, storage);

        // event spanning Oct 9th-11th
        LocalDateTime from = LocalDateTime.of(2025, 10, 9, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 10, 11, 0, 0);
        new AddEventCommand("testEvent", from, to).execute(tasks, ui, storage);

        // 9th, only event should appear
        new OnDateCommand(LocalDate.of(2025, 10, 9)).execute(tasks, ui, storage);
        String day9 = ui.lastBox();
        assertFalse(day9.contains("testDeadline"));
        assertTrue(day9.contains("testEvent"));

        // 10th, both should appear
        new OnDateCommand(LocalDate.of(2025, 10, 10)).execute(tasks, ui, storage);
        String day10 = ui.lastBox();
        assertTrue(day10.contains("testDeadline"));
        assertTrue(day10.contains("testEvent"));

        //11th, only event should appear
        new OnDateCommand(LocalDate.of(2025, 10, 11)).execute(tasks, ui, storage);
        String day11 = ui.lastBox();
        assertFalse(day11.contains("testDeadline"));
        assertTrue(day11.contains("testEvent"));

        // 12th, none should appear
        new OnDateCommand(LocalDate.of(2025, 10, 12)).execute(tasks, ui, storage);
        assertEquals("No tasks found on that date.", ui.lastBox());
    }

    @Test
    void onDate_emptyList_showsNoTasksMessage() {
        new OnDateCommand(LocalDate.of(2025, 1, 1)).execute(tasks, ui, storage);
        assertTrue(ui.lastBox().contains("No tasks found on that date"));
    }

    @Test
    void exitCommand_bye_showsGoodbyeMessageAndCloses() {
        new ExitCommand().execute(tasks, ui, storage);
        assertTrue(ui.goodbyeShown);
        assertTrue(ui.isClosed);
    }
}
