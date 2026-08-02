package com.proapps.taxianalytics.config;

import org.apache.spark.sql.SparkSession;

public final class SparkSessionProvider {
    private SparkSessionProvider() {
    }

    public static SparkSession create(String appName, String master) {
        return SparkSession.builder()
                .appName(appName)
                .master(master)
                .config("spark.sql.session.timeZone", "UTC")
                .config("spark.sql.shuffle.partitions", "4")
                .getOrCreate();
    }
}
