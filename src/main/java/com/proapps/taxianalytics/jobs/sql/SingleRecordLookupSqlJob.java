package com.proapps.taxianalytics.jobs.sql;

import com.proapps.taxianalytics.cli.CliArgs;
import com.proapps.taxianalytics.model.LookupCriteria;
import com.proapps.taxianalytics.model.TaxiColumns;
import com.proapps.taxianalytics.util.FileUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.SaveMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SingleRecordLookupSqlJob {
    private static final Logger log = LoggerFactory.getLogger(SingleRecordLookupSqlJob.class);

    private SingleRecordLookupSqlJob() {
    }

    public static void run(SparkSession spark, CliArgs cli) {
        LookupCriteria criteria = readCriteria(cli);
        String input = cli.getRequired("input");
        String output = cli.getRequired("output");

        Dataset<Row> taxi = spark.read().option("header", true).option("mode", "PERMISSIVE").csv(input);
        Dataset<Row> result = taxi.filter(
                functions.col(TaxiColumns.VENDOR_ID).equalTo(criteria.vendorId())
                        .and(functions.col(TaxiColumns.PICKUP_DATETIME).equalTo(criteria.pickupDatetime()))
                        .and(functions.col(TaxiColumns.DROPOFF_DATETIME).equalTo(criteria.dropoffDatetime()))
                        .and(functions.col(TaxiColumns.PASSENGER_COUNT).equalTo(criteria.passengerCount()))
                        .and(functions.col(TaxiColumns.TRIP_DISTANCE).equalTo(criteria.tripDistance()))
        );

        log.info("Single lookup completed");
        FileUtils.deleteIfExists(spark, output);
        result.write().mode(SaveMode.Overwrite).option("header", true).csv(output);
    }

    private static LookupCriteria readCriteria(CliArgs cli) {
        LookupCriteria defaults = LookupCriteria.defaults();
        return new LookupCriteria(
                cli.get("vendor-id", defaults.vendorId()),
                cli.get("pickup", defaults.pickupDatetime()),
                cli.get("dropoff", defaults.dropoffDatetime()),
                cli.get("passenger-count", defaults.passengerCount()),
                cli.get("trip-distance", defaults.tripDistance())
        );
    }
}
