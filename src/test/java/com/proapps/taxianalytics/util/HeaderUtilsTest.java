package com.proapps.taxianalytics.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HeaderUtilsTest {
    @Test
    void buildsIndexMap() {
        Map<String, Integer> map = HeaderUtils.indexByName(List.of("VendorID", "payment_type"));
        assertEquals(0, HeaderUtils.requiredIndex(map, "VendorID"));
        assertEquals(1, HeaderUtils.requiredIndex(map, "payment_type"));
    }

    @Test
    void throwsForMissingColumn() {
        Map<String, Integer> map = HeaderUtils.indexByName(List.of("VendorID"));
        assertThrows(IllegalArgumentException.class, () -> HeaderUtils.requiredIndex(map, "missing"));
    }
}
