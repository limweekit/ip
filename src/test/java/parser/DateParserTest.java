package parser;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateParserTest {

    // accepted date-time formats

    @Test
    void parseFlexibleDateTime_isoDate_HHmm_returnsExpectedDateTime() {
        LocalDateTime dt = DateParser.parseFlexibleDateTime("2025-08-26 2359");
        assertEquals(2025, dt.getYear());
        assertEquals(8, dt.getMonthValue());
        assertEquals(26, dt.getDayOfMonth());
        assertEquals(23, dt.getHour());
        assertEquals(59, dt.getMinute());
    }

    @Test
    void parseFlexibleDateTime_isoDate_colonHHMM_returnsExpectedDateTime() {
        LocalDateTime dt = DateParser.parseFlexibleDateTime("2025-08-26 23:59");
        assertEquals(2025, dt.getYear());
        assertEquals(8, dt.getMonthValue());
        assertEquals(26, dt.getDayOfMonth());
        assertEquals(23, dt.getHour());
        assertEquals(59, dt.getMinute());
    }

    @Test
    void parseFlexibleDateTime_slashDate_HHmm_returnsExpectedDateTime() {
        LocalDateTime dt = DateParser.parseFlexibleDateTime("26/8/2025 2359");
        assertEquals(2025, dt.getYear());
        assertEquals(8, dt.getMonthValue());
        assertEquals(26, dt.getDayOfMonth());
        assertEquals(23, dt.getHour());
        assertEquals(59, dt.getMinute());
    }

    @Test
    void parseFlexibleDateTime_slashDate_colonHHMM_returnsExpectedDateTime() {
        LocalDateTime dt = DateParser.parseFlexibleDateTime("26/8/2025 23:59");
        assertEquals(2025, dt.getYear());
        assertEquals(8, dt.getMonthValue());
        assertEquals(26, dt.getDayOfMonth());
        assertEquals(23, dt.getHour());
        assertEquals(59, dt.getMinute());
    }

    @Test
    void parseFlexibleDateTime_isoLocalDateTime_returnsExpectedDateTime() {
        LocalDateTime dt = DateParser.parseFlexibleDateTime("2025-08-26T23:59");
        assertEquals(2025, dt.getYear());
        assertEquals(8, dt.getMonthValue());
        assertEquals(26, dt.getDayOfMonth());
        assertEquals(23, dt.getHour());
        assertEquals(59, dt.getMinute());
    }

    // accepted date-only formats

    @Test
    void parseFlexibleDateTime_isoDateOnly_returnsStartOfDay() {
        LocalDateTime dt = DateParser.parseFlexibleDateTime("2025-08-26");
        assertEquals(LocalDate.of(2025, 8, 26), dt.toLocalDate());
        assertEquals(0, dt.getHour());
        assertEquals(0, dt.getMinute());
    }

    @Test
    void parseFlexibleDateTime_slashDateOnly_returnsStartOfDay() {
        LocalDateTime dt = DateParser.parseFlexibleDateTime("26/8/2025");
        assertEquals(LocalDate.of(2025, 8, 26), dt.toLocalDate());
        assertEquals(0, dt.getHour());
        assertEquals(0, dt.getMinute());
    }

    @Test
    void parseFlexibleDateTime_mmmDdYyyy_returnsStartOfDay() {
        LocalDateTime dt = DateParser.parseFlexibleDateTime("Aug 26 2025");
        assertEquals(LocalDate.of(2025, 8, 26), dt.toLocalDate());
        assertEquals(0, dt.getHour());
        assertEquals(0, dt.getMinute());
    }

    // whitespace testing

    @Test
    void parseFlexibleDateTime_surroundingWhitespace_trimmedAndParsed() {
        LocalDateTime dt = DateParser.parseFlexibleDateTime("   2025-08-26 2359   ");
        assertEquals(23, dt.getHour());
        assertEquals(59, dt.getMinute());
    }

    // invalid inputs

    @Test
    void parseFlexibleDateTime_invalidSlash_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> DateParser.parseFlexibleDateTime("2025/08/26"));
    }

    @Test
    void parseFlexibleDateTime_invalidDash_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> DateParser.parseFlexibleDateTime("26-08-2025"));
    }

    @Test
    void parseFlexibleDateTime_invalidComma_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> DateParser.parseFlexibleDateTime("Aug 26, 2025"));
    }

    @Test
    void parseFlexibleDateTime_notADate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> DateParser.parseFlexibleDateTime("notADate"));
    }

    @Test
    void parseFlexibleDateTime_emptyString_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> DateParser.parseFlexibleDateTime(""));
    }

    @Test
    void parseFlexibleDateTime_spacesOnly_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> DateParser.parseFlexibleDateTime("   "));
    }

    // output formatting helpers

    @Test
    void outDate_formatLocalDate_returnsExpectedString() {
        String s = LocalDate.of(2025, 8, 9).format(DateParser.OUT_DATE);
        assertEquals("Aug 09 2025", s);
    }

    @Test
    void outDateTime_formatLocalDateTime_returnsExpectedString() {
        String s = LocalDateTime.of(2025, 8, 9, 6, 5)
                .format(DateParser.OUT_DATE_TIME);
        assertEquals("Aug 09 2025 06:05", s);
    }
}
