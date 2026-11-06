package ru.javawebinar.topjava.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private TimeUtil() {
        /* This utility class should not be instantiated */
    }

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return !lt.isBefore(startTime) && lt.isBefore(endTime);
    }
}
