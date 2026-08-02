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

import java.util.LinkedHashMap;
import java.util.Map;

public final class BenchmarkRunner {
    private static final Logger log = LoggerFactory.getLogger(BenchmarkRunner.class);

    private BenchmarkRunner() {
    }

    public static void run(SparkSession spark, CliArgs cli) {
        Map<String, Long> timings = new LinkedHashMap<>();

        timings.put("sql.lookup", measure(() -> SingleRecordLookupSqlJob.run(spark, cli)));
        timings.put("sql.filter", measure(() -> RateCodeFilterSqlJob.run(spark, cli)));
        timings.put("sql.payment", measure(() -> PaymentTypeAnalyticsSqlJob.run(spark, cli)));
        timings.put("rdd.lookup", measure(() -> SingleRecordLookupRddJob.run(spark, cli)));
        timings.put("rdd.filter", measure(() -> RateCodeFilterRddJob.run(spark, cli)));
        timings.put("rdd.payment", measure(() -> PaymentTypeAnalyticsRddJob.run(spark, cli)));

        log.info("Benchmark timings (ms):");
        timings.forEach((name, value) -> log.info("{} = {}", name, value));
    }

    private static long measure(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        return (System.nanoTime() - start) / 1_000_000;
    }
}
