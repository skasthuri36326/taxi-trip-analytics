package com.proapps.taxianalytics.jobs.rdd;

import com.proapps.taxianalytics.cli.CliArgs;
import com.proapps.taxianalytics.model.LookupCriteria;
import com.proapps.taxianalytics.model.TaxiColumns;
import com.proapps.taxianalytics.util.FileUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public final class SingleRecordLookupRddJob {
    private static final Logger log = LoggerFactory.getLogger(SingleRecordLookupRddJob.class);

    private SingleRecordLookupRddJob() {
    }

    public static void run(SparkSession spark, CliArgs cli) {
        LookupCriteria criteria = new LookupCriteria(
                cli.get("vendor-id", LookupCriteria.defaults().vendorId()),
                cli.get("pickup", LookupCriteria.defaults().pickupDatetime()),
                cli.get("dropoff", LookupCriteria.defaults().dropoffDatetime()),
                cli.get("passenger-count", LookupCriteria.defaults().passengerCount()),
                cli.get("trip-distance", LookupCriteria.defaults().tripDistance())
        );

        String input = cli.getRequired("input");
        String output = cli.getRequired("output");

        /*
         * Spark serializes the lambda used by RDD.filter().
         * Do not capture the LookupCriteria record itself because it is not
         * Serializable. Capture only its String values instead.
         */
        String vendorId = criteria.vendorId();
        String pickupDatetime = criteria.pickupDatetime();
        String dropoffDatetime = criteria.dropoffDatetime();
        String passengerCount = criteria.passengerCount();
        String tripDistance = criteria.tripDistance();

        JavaSparkContext jsc =
                JavaSparkContext.fromSparkContext(spark.sparkContext());

        JavaRDD<String> lines = jsc.textFile(input);

        String header = lines.first();

        List<String> headers = Arrays.asList(
                header.split(",", -1)
        );

        int vendorIndex =
                headers.indexOf(TaxiColumns.VENDOR_ID);

        int pickupIndex =
                headers.indexOf(TaxiColumns.PICKUP_DATETIME);

        int dropoffIndex =
                headers.indexOf(TaxiColumns.DROPOFF_DATETIME);

        int passengerIndex =
                headers.indexOf(TaxiColumns.PASSENGER_COUNT);

        int tripDistanceIndex =
                headers.indexOf(TaxiColumns.TRIP_DISTANCE);

        JavaRDD<String> result = lines
                .filter(line -> !line.equals(header))
                .filter(line -> {
                    String[] values = line.split(",", -1);

                    return values.length >= headers.size()
                            && values[vendorIndex].equals(vendorId)
                            && values[pickupIndex].equals(pickupDatetime)
                            && values[dropoffIndex].equals(dropoffDatetime)
                            && values[passengerIndex].equals(passengerCount)
                            && values[tripDistanceIndex].equals(tripDistance);
                });

        log.info("RDD single lookup completed");

        FileUtils.deleteIfExists(spark, output);

        result.saveAsTextFile(output);
    }
}