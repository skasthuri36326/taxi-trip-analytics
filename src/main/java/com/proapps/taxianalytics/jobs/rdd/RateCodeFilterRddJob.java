package com.proapps.taxianalytics.jobs.rdd;

import com.proapps.taxianalytics.cli.CliArgs;
import com.proapps.taxianalytics.model.TaxiColumns;
import com.proapps.taxianalytics.util.FileUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public final class RateCodeFilterRddJob {
    private static final Logger log = LoggerFactory.getLogger(RateCodeFilterRddJob.class);

    private RateCodeFilterRddJob() {
    }

    public static void run(SparkSession spark, CliArgs cli) {
        String input = cli.getRequired("input");
        String output = cli.getRequired("output");
        String rateCodeId = cli.get("ratecode-id", "4");

        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
        JavaRDD<String> lines = jsc.textFile(input);
        String header = lines.first();
        List<String> headers = Arrays.asList(header.split(",", -1));
        int rateIndex = headers.indexOf(TaxiColumns.RATE_CODE_ID);

        JavaRDD<String> result = lines
                .filter(line -> !line.equals(header))
                .filter(line -> {
                    String[] values = line.split(",", -1);
                    return values.length >= headers.size() && values[rateIndex].equals(rateCodeId);
                });

        log.info("RDD rate code {} filter completed", rateCodeId);
        FileUtils.deleteIfExists(spark, output);
        result.saveAsTextFile(output);
    }
}
