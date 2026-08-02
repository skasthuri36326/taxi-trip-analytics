package com.proapps.taxianalytics.model;

public record LookupCriteria(
        String vendorId,
        String pickupDatetime,
        String dropoffDatetime,
        String passengerCount,
        String tripDistance
) {
    public static LookupCriteria defaults() {
        return new LookupCriteria("2", "2017-10-01 00:15:30", "2017-10-01 00:25:11", "1", "2.17");
    }
}
