package com.proapps.taxianalytics.cli;

import com.proapps.taxianalytics.benchmark.BenchmarkRunner;
import com.proapps.taxianalytics.config.SparkSessionProvider;
import com.proapps.taxianalytics.exception.InvalidJobException;
import com.proapps.taxianalytics.jobs.rdd.PaymentTypeAnalyticsRddJob;
import com.proapps.taxianalytics.jobs.rdd.RateCodeFilterRddJob;
import com.proapps.taxianalytics.jobs.rdd.SingleRecordLookupRddJob;
import com.proapps.taxianalytics.jobs.sql.PaymentTypeAnalyticsSqlJob;
import com.proapps.taxianalytics.jobs.sql.RateCodeFilterSqlJob;
import com.proapps.taxianalytics.jobs.sql.SingleRecordLookupSqlJob;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private Application() {
    }

    public static void main(String[] args) {
        CliArgs cli = CliArgs.parse(args);
        String engine = cli.getRequired("engine").toLowerCase();
        String job = cli.getRequired("job").toLowerCase();
        String master = cli.get("master", "local[*]");
        String appName = cli.get("app-name", "Taxi Trip Analytics");

        try (SparkSession spark = SparkSessionProvider.create(appName, master)) {
            if ("benchmark".equals(job) || "benchmark".equals(engine)) {
                BenchmarkRunner.run(spark, cli);
                return;
            }
            switch (engine) {
                case "sql" -> runSqlJob(spark, job, cli);
                case "rdd" -> runRddJob(spark, job, cli);
                default -> throw new InvalidJobException("Unknown engine: " + engine);
            }
        }
    }

    private static void runSqlJob(SparkSession spark, String job, CliArgs cli) {
        switch (job) {
            case "lookup" -> SingleRecordLookupSqlJob.run(spark, cli);
            case "filter" -> RateCodeFilterSqlJob.run(spark, cli);
            case "payment" -> PaymentTypeAnalyticsSqlJob.run(spark, cli);
            default -> throw new InvalidJobException("Unknown SQL job: " + job);
        }
    }

    private static void runRddJob(SparkSession spark, String job, CliArgs cli) {
        switch (job) {
            case "lookup" -> SingleRecordLookupRddJob.run(spark, cli);
            case "filter" -> RateCodeFilterRddJob.run(spark, cli);
            case "payment" -> PaymentTypeAnalyticsRddJob.run(spark, cli);
            default -> throw new InvalidJobException("Unknown RDD job: " + job);
        }
    }
}
