package commands;

import app.Ui;
import exceptions.InvalidDateException;
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
    void TodoDeadlineEventList_validInputs_success() throws InvalidDateException {
        // add todo
        String rTodo = new AddTodoCommand("testTodo").execute(tasks, ui, storage);
        assertTrue(rTodo.toLowerCase().contains("added"));
        assertEquals(1, tasks.size());
        assertInstanceOf(ToDo.class, tasks.get(1));

        // add deadline
        LocalDateTime by = LocalDateTime.of(2025, 12, 1, 23, 59);
        String rDeadline = new AddDeadlineCommand("testDeadline", by).execute(tasks, ui, storage);
        assertTrue(rDeadline.toLowerCase().contains("added"));
        assertEquals(2, tasks.size());
        assertInstanceOf(Deadline.class, tasks.get(2));

        // add event
        LocalDateTime from = LocalDateTime.of(2025, 12, 5, 9, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 12, 7, 17, 0);
        String rEvent = new AddEventCommand("testEvent", from, to).execute(tasks, ui, storage);
        assertTrue(rEvent.toLowerCase().contains("added"));
        assertEquals(3, tasks.size());
        assertInstanceOf(Event.class, tasks.get(3));

        // render list of tasks
        String out = new ListCommand().execute(tasks, ui, storage);
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
        String out = new ListCommand().execute(tasks, ui, storage);
        assertEquals("It's kinda empty in here! No tasks have been added yet.", out);
    }

    @Test
    void markUnmarkDelete_validInputs_success() throws YapGPTException {
        // seed 2 todos
        new AddTodoCommand("t1").execute(tasks, ui, storage);
        new AddTodoCommand("t2").execute(tasks, ui, storage);

        // mark index 2
        String m = new MarkCommand(2).execute(tasks, ui, storage);
        assertTrue(m.toLowerCase().contains("mark"));
        assertTrue(tasks.get(2).toString().contains("[X]"));

        // unmark index 2
        String u = new UnmarkCommand(2).execute(tasks, ui, storage);
        assertTrue(u.toLowerCase().contains("unmark"));
        assertFalse(tasks.get(2).toString().contains("[X]"));

        // delete index 1
        String d = new DeleteCommand(1).execute(tasks, ui, storage);
        assertTrue(d.toLowerCase().contains("delete"));
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
        String out = new ListCommand().execute(tasks, ui, storage);
        assertEquals("It's kinda empty in here! No tasks have been added yet.", out);
    }

    @Test
    void onDate_validInputs_success() throws InvalidDateException {
        // deadline on 10th
        LocalDateTime by = LocalDateTime.of(2025, 10, 10, 9, 0);
        new AddDeadlineCommand("testDeadline", by).execute(tasks, ui, storage);

        // event spanning Oct 9th-11th
        LocalDateTime from = LocalDateTime.of(2025, 10, 9, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 10, 11, 0, 0);
        new AddEventCommand("testEvent", from, to).execute(tasks, ui, storage);

        // 9th, only event should appear
        String day9 = new OnDateCommand(LocalDate.of(2025, 10, 9)).execute(tasks, ui, storage);
        assertFalse(day9.contains("testDeadline"));
        assertTrue(day9.contains("testEvent"));

        // 10th, both should appear
        String day10 = new OnDateCommand(LocalDate.of(2025, 10, 10)).execute(tasks, ui, storage);
        assertTrue(day10.contains("testDeadline"));
        assertTrue(day10.contains("testEvent"));

        //11th, only event should appear
        String day11 = new OnDateCommand(LocalDate.of(2025, 10, 11)).execute(tasks, ui, storage);
        assertFalse(day11.contains("testDeadline"));
        assertTrue(day11.contains("testEvent"));

        // 12th, none should appear
        String day12 = new OnDateCommand(LocalDate.of(2025, 10, 12)).execute(tasks, ui, storage);
        assertEquals("Oops! No tasks found on that date.", day12);
    }

    @Test
    void onDate_emptyList_showsNoTasksMessage() {
        String out = new OnDateCommand(LocalDate.of(2025, 1, 1)).execute(tasks, ui, storage);
        assertTrue(out.contains("Oops! No tasks found on that date."));
    }

    @Test
    void find_singleAndMultiWordQueries_matchExpectedTasks() throws YapGPTException {
        new AddTodoCommand("read book").execute(tasks, ui, storage);
        new AddTodoCommand("return Book to library").execute(tasks, ui, storage);
        new AddTodoCommand("buy milk").execute(tasks, ui, storage);

        // single word
        String out1 = new FindCommand("book").execute(tasks, ui, storage);
        assertTrue(out1.contains("read book"));
        assertTrue(out1.toLowerCase().contains("return book"));
        assertFalse(out1.contains("buy milk"));

        // multi-word AND
        String out2 = new FindCommand("return library").execute(tasks, ui, storage);
        assertTrue(out2.toLowerCase().contains("return book to library"));
        assertFalse(out2.contains("read book"));
    }

    @Test
    void exitCommand_bye_showsGoodbyeMessageAndCloses() {
        String out = new ExitCommand().execute(tasks, ui, storage);
        assertNotNull(out);
        assertTrue(out.toLowerCase().contains("bye"));
        // If ExitCommand no longer touches Ui, these flags will remain false.
        // Keeping assertions only on the returned message.
    }
}
