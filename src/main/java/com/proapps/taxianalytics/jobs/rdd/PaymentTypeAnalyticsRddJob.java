package com.proapps.taxianalytics.jobs.rdd;

import com.proapps.taxianalytics.cli.CliArgs;
import com.proapps.taxianalytics.model.TaxiColumns;
import com.proapps.taxianalytics.util.FileUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class PaymentTypeAnalyticsRddJob {
    private static final Logger log = LoggerFactory.getLogger(PaymentTypeAnalyticsRddJob.class);

    private PaymentTypeAnalyticsRddJob() {
    }

    public static void run(SparkSession spark, CliArgs cli) {
        String input = cli.getRequired("input");
        String output = cli.getRequired("output");

        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
        JavaRDD<String> lines = jsc.textFile(input);
        String header = lines.first();
        List<String> headers = Arrays.asList(header.split(",", -1));
        int paymentIndex = headers.indexOf(TaxiColumns.PAYMENT_TYPE);

        JavaRDD<String> data = lines
                .filter(line -> !line.equals(header))
                .filter(line -> line.split(",", -1).length >= headers.size());

        JavaRDD<Tuple2<String, Long>> counts = data
                .mapToPair((PairFunction<String, String, Long>) line -> {
                    String[] values = line.split(",", -1);
                    return new Tuple2<>(values[paymentIndex], 1L);
                })
                .reduceByKey(Long::sum)
                .map(tuple -> new Tuple2<>(tuple._1, tuple._2))
                .sortBy(tuple -> tuple._2, true, 1);

        JavaRDD<String> outputLines = counts.map(tuple -> tuple._1 + "," + tuple._2);
        log.info("RDD payment analytics completed");
        FileUtils.deleteIfExists(spark, output);
        outputLines.saveAsTextFile(output);
    }
}
