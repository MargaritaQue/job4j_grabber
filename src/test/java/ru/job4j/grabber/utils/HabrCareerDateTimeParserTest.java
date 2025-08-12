package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HabrCareerDateTimeParserTest {

    private final HabrCareerDateTimeParser dateParser = new HabrCareerDateTimeParser();

    @Test
    void testParseWithValidISOOffsetDateTime() {
        String input = "2025-08-10T09:59:40+03:00";
        LocalDateTime result = dateParser.parse(input);
        LocalDateTime expected = LocalDateTime.of(2025, 8, 10, 9, 59, 40);
        assertEquals(expected, result);
    }

    @Test
    public void testParseWithUTC() {
        String input = "2023-12-01T15:30:00+00:00";
        LocalDateTime result = dateParser.parse(input);
        LocalDateTime expected = LocalDateTime.of(2023, 12, 1, 15, 30, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseInvalidFormat() {
        String invalidInput = "Invalid date string";
        assertThrows(Exception.class, () ->
            dateParser.parse(invalidInput)
        );
    }
}