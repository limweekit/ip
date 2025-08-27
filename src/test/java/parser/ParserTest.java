package parser;

import commands.*;
import exceptions.EmptyDescriptionException;
import exceptions.InvalidDateException;
import exceptions.UnknownCommandException;
import exceptions.YapGPTException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    // bye

    @Test
    void parse_nullInput_returnsExitCommand() throws YapGPTException {
        assertInstanceOf(ExitCommand.class, Parser.parse(null));
    }

    @Test
    void parse_byeCaseInsensitive_returnsExitCommand() throws YapGPTException {
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
        assertInstanceOf(ExitCommand.class, Parser.parse("BYE"));
    }

    //list

    @Test
    void parse_listCaseInsensitive_returnsListCommand() throws YapGPTException {
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(ListCommand.class, Parser.parse("LiSt"));
    }

    // todo

    @Test
    void parse_todoValid_returnsAddTodoCommand() throws YapGPTException {
        assertInstanceOf(AddTodoCommand.class, Parser.parse("todo buy milk"));
    }

    @Test
    void parse_todoEmpty_throwsEmptyDescriptionException() {
        assertThrows(EmptyDescriptionException.class, () -> Parser.parse("todo   "));
    }

    // deadline

    @Test
    void parse_deadlineIsoWithTime_returnsAddDeadlineCommand() throws YapGPTException {
        assertInstanceOf(AddDeadlineCommand.class, Parser.parse("deadline submit /by 2025-12-01 23:59"));
    }

    @Test
    void parse_deadlineIsoDateOnly_returnsAddDeadlineCommand() throws YapGPTException {
        assertInstanceOf(AddDeadlineCommand.class, Parser.parse("deadline submit /by 2025-12-01"));
    }

    @Test
    void parse_deadlineSlashFormats_returnsAddDeadlineCommand() throws YapGPTException {
        assertInstanceOf(AddDeadlineCommand.class, Parser.parse("deadline submit /by 1/12/2025 2359"));
        assertInstanceOf(AddDeadlineCommand.class, Parser.parse("deadline submit /by 1/12/2025 23:59"));
    }

    @Test
    void parse_deadlineMissingDateToken_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> Parser.parse("deadline submit"));
    }

    @Test
    void parse_deadlineMissingDescription_throwsEmptyDescriptionException() {
        assertThrows(EmptyDescriptionException.class, () -> Parser.parse("deadline /by 2025-12-01"));
    }

    @Test
    void parse_deadlineInvalidDate_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> Parser.parse("deadline submit /by 2025/12/01"));
    }

    // event

    @Test
    void parse_eventWithTimes_returnsAddEventCommand() throws YapGPTException {
        assertInstanceOf(AddEventCommand.class, Parser.parse("event meet /from 2025-12-05 10:00 /to 2025-12-05 12:00"));
    }

    @Test
    void parse_eventDateOnly_returnsAddEventCommand() throws YapGPTException {
        assertInstanceOf(AddEventCommand.class, Parser.parse("event camp /from 2025-12-05 /to 2025-12-07"));
    }

    @Test
    void parse_eventMissingFrom_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> Parser.parse("event camp /to 2025-12-07"));
    }

    @Test
    void parse_eventMissingTo_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> Parser.parse("event camp /from 2025-12-05"));
    }

    @Test
    void parse_eventMissingDescription_throwsEmptyDescriptionException() {
        assertThrows(EmptyDescriptionException.class,
                () -> Parser.parse("event    /from 2025-12-05 /to 2025-12-07"));
    }

    @Test
    void parse_eventInvalidFrom_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class,
                () -> Parser.parse("event x /from notADate /to 2025-12-07"));
    }

    @Test
    void parse_eventInvalidTo_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class,
                () -> Parser.parse("event x /from 2025-12-05 /to notADate"));
    }

    // mark / unmark / delete

    @Test
    void parse_markValid_returnsMarkCommand() throws YapGPTException {
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
    }

    @Test
    void parse_markMissingNumber_throwsEmptyDescriptionException() {
        assertThrows(EmptyDescriptionException.class, () -> Parser.parse("mark"));
    }

    @Test
    void parse_markNotANumber_throwsUnknownCommandException() {
        assertThrows(UnknownCommandException.class, () -> Parser.parse("mark !"));
    }

    @Test
    void parse_markZeroOrNegative_returnsMarkCommand() throws YapGPTException {
        // Parser does not validate positivity; range enforced later
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 0"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark -2"));
    }

    @Test
    void parse_unmarkValid_returnsUnmarkCommand() throws YapGPTException {
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 3"));
    }

    @Test
    void parse_unmarkMissingNumber_throwsEmptyDescriptionException() {
        assertThrows(EmptyDescriptionException.class, () -> Parser.parse("unmark"));
    }

    @Test
    void parse_unmarkNotANumber_throwsUnknownCommandException() {
        assertThrows(UnknownCommandException.class, () -> Parser.parse("unmark !"));
    }

    @Test
    void parse_deleteValid_returnsDeleteCommand() throws YapGPTException {
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 2"));
    }

    @Test
    void parse_deleteMissingNumber_throwsEmptyDescriptionException() {
        assertThrows(EmptyDescriptionException.class, () -> Parser.parse("delete   "));
    }

    @Test
    void parse_deleteNotANumber_throwsUnknownCommandException() {
        assertThrows(UnknownCommandException.class, () -> Parser.parse("delete abc"));
    }

    // on date

    @Test
    void parse_onValidDateFormats_returnsOnDateCommand() throws YapGPTException {
        assertInstanceOf(OnDateCommand.class, Parser.parse("on 2025-10-10"));
        assertInstanceOf(OnDateCommand.class, Parser.parse("on Oct 10 2025"));
        assertInstanceOf(OnDateCommand.class, Parser.parse("on 10/10/2025"));
    }

    @Test
    void parse_onBadDate_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> Parser.parse("on notADate"));
    }

    // find

    @Test
    void parse_findValid_returnsFindCommand() throws YapGPTException {
        assertInstanceOf(FindCommand.class, Parser.parse("find book"));
    }

    @Test
    void parse_findEmpty_throwsEmptyDescriptionException() {
        assertThrows(EmptyDescriptionException.class, () -> Parser.parse("find   "));
    }

    // unknown commands

    @Test
    void parse_unknownCommand_throwsUnknownCommandException() {
        assertThrows(UnknownCommandException.class, () -> Parser.parse("unknownCommand"));
    }
}
