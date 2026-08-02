package com.proapps.taxianalytics.util;

import java.util.List;

public final class ValidationUtils {
    private ValidationUtils() {
    }

    public static boolean hasExpectedColumns(String[] values, int expected) {
        return values != null && values.length == expected;
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static void requireAllPresent(String... values) {
        for (String value : values) {
            if (isBlank(value)) {
                throw new IllegalArgumentException("Missing required value");
            }
        }
    }
}
