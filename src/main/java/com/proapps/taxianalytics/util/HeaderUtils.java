package com.proapps.taxianalytics.util;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class HeaderUtils {
    private HeaderUtils() {
    }

    public static Map<String, Integer> indexByName(List<String> headers) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            map.put(headers.get(i).trim().toLowerCase(Locale.ROOT), i);
        }
        return map;
    }

    public static int requiredIndex(Map<String, Integer> indexMap, String name) {
        Integer index = indexMap.get(name.toLowerCase(Locale.ROOT));
        if (index == null) {
            throw new IllegalArgumentException("Required column not found: " + name);
        }
        return index;
    }
}
