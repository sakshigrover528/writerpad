package com.xebia.fs101.model;

public class ReadingTime {
    private int minutes;
    private int seconds;

    public ReadingTime() {
    }

    public ReadingTime(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
}
