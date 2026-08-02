package com.proapps.taxianalytics.jobs.sql;

import com.proapps.taxianalytics.cli.CliArgs;
import com.proapps.taxianalytics.model.TaxiColumns;
import com.proapps.taxianalytics.util.FileUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PaymentTypeAnalyticsSqlJob {
    private static final Logger log = LoggerFactory.getLogger(PaymentTypeAnalyticsSqlJob.class);

    private PaymentTypeAnalyticsSqlJob() {
    }

    public static void run(SparkSession spark, CliArgs cli) {
        String input = cli.getRequired("input");
        String output = cli.getRequired("output");

        Dataset<Row> taxi = spark.read().option("header", true).option("mode", "PERMISSIVE").csv(input);
        Dataset<Row> result = taxi
                .filter(functions.col(TaxiColumns.PAYMENT_TYPE).isNotNull())
                .groupBy(functions.col(TaxiColumns.PAYMENT_TYPE))
                .count()
                .orderBy(functions.col("count").asc(), functions.col(TaxiColumns.PAYMENT_TYPE).asc());

        log.info("Payment type analytics completed");
        FileUtils.deleteIfExists(spark, output);
        result.write().mode(SaveMode.Overwrite).option("header", true).csv(output);
    }
}
