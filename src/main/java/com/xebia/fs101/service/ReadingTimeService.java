package com.xebia.fs101.service;

import com.xebia.fs101.domain.ReadingTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.xebia.fs101.utils.StringUtils.findTotalWords;

@Component
public class ReadingTimeService {

    @Value("${spring.service.speed.per.second}")
    double speed;
    private int numberOfWords;
    private int minutes;
    private int seconds;

    public ReadingTime calculateReadingTime(String content) {
        this.numberOfWords = calculateNumberOfWords(content);
        this.minutes = (int) (numberOfWords / speed);
        this.seconds = (int) ((numberOfWords % speed) / (speed / 60));
        return new ReadingTime(minutes, seconds);
    }

    public Duration calculateReadingTimeInSeconds(String content) {
        int totalWords = findTotalWords(content);
        int readingTimeInSeconds = (int) (totalWords / speed);
        return Duration.of(readingTimeInSeconds, ChronoUnit.SECONDS);
    }

    private int calculateNumberOfWords(String content) {
        return content.split("\\s|\\.").length;

    }
}
