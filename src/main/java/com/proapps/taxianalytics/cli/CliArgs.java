package com.proapps.taxianalytics.cli;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CliArgs {
    private final Map<String, String> values;

    private CliArgs(Map<String, String> values) {
        this.values = values;
    }

    public static CliArgs parse(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String current = args[i];
            if (!current.startsWith("--")) {
                continue;
            }
            String key = current.substring(2).toLowerCase();
            String value = "true";
            if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                value = args[++i];
            }
            map.put(key, value);
        }
        return new CliArgs(Collections.unmodifiableMap(map));
    }

    public String getRequired(String key) {
        String value = values.get(key.toLowerCase());
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required argument: --" + key);
        }
        return value;
    }

    public String get(String key, String defaultValue) {
        return values.getOrDefault(key.toLowerCase(), defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        String value = values.get(key.toLowerCase());
        return value == null || value.isBlank() ? defaultValue : Integer.parseInt(value);
    }

    public double getDouble(String key, double defaultValue) {
        String value = values.get(key.toLowerCase());
        return value == null || value.isBlank() ? defaultValue : Double.parseDouble(value);
    }

    public Map<String, String> asMap() {
        return values;
    }
}
