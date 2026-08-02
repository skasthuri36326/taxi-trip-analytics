package com.proapps.taxianalytics.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaxiCsvParserTest {
    @Test
    void parsesCsvLine() {
        var parsed = TaxiCsvParser.parseLine("1,2,3,4");
        assertTrue(parsed.isPresent());
        assertEquals(4, parsed.get().length);
    }
}
