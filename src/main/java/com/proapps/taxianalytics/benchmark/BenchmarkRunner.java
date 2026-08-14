package com.proapps.taxianalytics.benchmark;

import com.proapps.taxianalytics.cli.CliArgs;
import com.proapps.taxianalytics.jobs.rdd.PaymentTypeAnalyticsRddJob;
import com.proapps.taxianalytics.jobs.rdd.RateCodeFilterRddJob;
import com.proapps.taxianalytics.jobs.rdd.SingleRecordLookupRddJob;
import com.proapps.taxianalytics.jobs.sql.PaymentTypeAnalyticsSqlJob;
import com.proapps.taxianalytics.jobs.sql.RateCodeFilterSqlJob;
import com.proapps.taxianalytics.jobs.sql.SingleRecordLookupSqlJob;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BenchmarkRunner {
    private static final Logger log =
            LoggerFactory.getLogger(BenchmarkRunner.class);

    private BenchmarkRunner() {
    }

    public static void run(SparkSession spark, CliArgs cli) {
        Map<String, Long> timings = new LinkedHashMap<>();

        timings.put(
                "sql.lookup",
                measure(() -> SingleRecordLookupSqlJob.run(spark, cli))
        );

        timings.put(
                "sql.filter",
                measure(() -> RateCodeFilterSqlJob.run(spark, cli))
        );

        timings.put(
                "sql.payment",
                measure(() -> PaymentTypeAnalyticsSqlJob.run(spark, cli))
        );

        timings.put(
                "rdd.lookup",
                measure(() -> SingleRecordLookupRddJob.run(spark, cli))
        );

        timings.put(
                "rdd.filter",
                measure(() -> RateCodeFilterRddJob.run(spark, cli))
        );

        timings.put(
                "rdd.payment",
                measure(() -> PaymentTypeAnalyticsRddJob.run(spark, cli))
        );

        log.info("Benchmark timings (ms):");

        timings.forEach(
                (name, value) -> log.info("{} = {}", name, value)
        );

        writeResults(cli, timings);
    }

    private static long measure(Runnable runnable) {
        long start = System.nanoTime();

        runnable.run();

        return (System.nanoTime() - start) / 1_000_000;
    }

    private static void writeResults(
            CliArgs cli,
            Map<String, Long> timings
    ) {
        String outputDirectory =
                cli.get("output", "output/benchmark");

        Path directory = Paths.get(outputDirectory);

        Path resultFile =
                directory.resolve("benchmark-results.csv");

        StringBuilder csv = new StringBuilder();

        csv.append("workload,time_ms")
                .append(System.lineSeparator());

        timings.forEach((workload, time) ->
                csv.append(workload)
                        .append(",")
                        .append(time)
                        .append(System.lineSeparator())
        );

        try {
            Files.createDirectories(directory);

            Files.writeString(
                    resultFile,
                    csv.toString(),
                    StandardCharsets.UTF_8
            );

            log.info(
                    "Benchmark results written to {}",
                    resultFile.toAbsolutePath()
            );

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to write benchmark results to "
                            + resultFile,
                    e
            );
        }
    }
}