package com.proapps.taxianalytics.model;

public final class TaxiColumns {
    private TaxiColumns() {
    }

    public static final String VENDOR_ID = "VendorID";
    public static final String PICKUP_DATETIME = "tpep_pickup_datetime";
    public static final String DROPOFF_DATETIME = "tpep_dropoff_datetime";
    public static final String PASSENGER_COUNT = "passenger_count";
    public static final String TRIP_DISTANCE = "trip_distance";
    public static final String RATE_CODE_ID = "RatecodeID";
    public static final String PAYMENT_TYPE = "payment_type";

    public static final String[] ALL_COLUMNS = {
            VENDOR_ID,
            PICKUP_DATETIME,
            DROPOFF_DATETIME,
            PASSENGER_COUNT,
            TRIP_DISTANCE,
            RATE_CODE_ID,
            "store_and_fwd_flag",
            "PULocationID",
            "DOLocationID",
            PAYMENT_TYPE,
            "fare_amount",
            "extra",
            "mta_tax",
            "tip_amount",
            "tolls_amount",
            "improvement_surcharge",
            "total_amount"
    };
}
