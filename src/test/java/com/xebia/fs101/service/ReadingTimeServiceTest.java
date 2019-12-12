package com.xebia.fs101.service;

import com.xebia.fs101.domain.ReadingTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReadingTimeServiceTest {

    private ReadingTimeService readingTimeService;

    @BeforeEach
    void setUp() {
        readingTimeService = new ReadingTimeService();
        readingTimeService.speed = 20;
    }

    @Test
    void should_calculate_reading_time_of_the_given_string() {
        String content = "A paragraph is a series of related sentences "
                + "developing a central idea, called the topic. "
                + "Try to think about paragraphs in terms of thematic unity:"
                + " a paragraph is a sentence or a group of sentences that supports "
                + "one central, unified idea. Paragraphs add one idea at a time to your broader"
                + " argument.";
        ReadingTime readingTime = readingTimeService.calculateReadingTime(content);
        assertThat(readingTime.getMinutes()).isEqualTo(2);
        assertThat(readingTime.getSeconds()).isEqualTo(42);

    }
}