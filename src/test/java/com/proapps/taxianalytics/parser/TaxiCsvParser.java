package com.proapps.taxianalytics.parser;

import java.util.Optional;

public class TaxiCsvParser {

    private TaxiCsvParser() {
        // Utility class
    }

    public static Optional<String[]> parseLine(String line) {
        if (line == null || line.isBlank()) {
            return Optional.empty();
        }

        String[] fields = line.split(",");

        return Optional.of(fields);
    }
}