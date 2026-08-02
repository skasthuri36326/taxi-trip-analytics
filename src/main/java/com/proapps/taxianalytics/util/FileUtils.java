package com.proapps.taxianalytics.util;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;

public final class FileUtils {
    private FileUtils() {
    }

    public static void deleteIfExists(SparkSession spark, String outputPath) {
        try {
            FileSystem fs = FileSystem.get(spark.sparkContext().hadoopConfiguration());
            Path path = new Path(outputPath);
            if (fs.exists(path)) {
                fs.delete(path, true);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to clean output path: " + outputPath, e);
        }
    }
}
