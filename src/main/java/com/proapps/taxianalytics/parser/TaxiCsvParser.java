package com.proapps.taxianalytics.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

public final class TaxiCsvParser {
    private TaxiCsvParser() {
    }

    public static Optional<String[]> parseLine(String line) {
        if (line == null || line.isBlank()) {
            return Optional.empty();
        }

        try (CSVParser parser = CSVParser.parse(new StringReader(line), CSVFormat.DEFAULT)) {
            if (parser.getRecords().isEmpty()) {
                return Optional.empty();
            }
            CSVRecord record = parser.getRecords().get(0);
            String[] values = new String[record.size()];
            for (int i = 0; i < record.size(); i++) {
                values[i] = record.get(i).trim();
            }
            return Optional.of(values);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
