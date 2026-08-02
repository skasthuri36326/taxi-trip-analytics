package com.proapps.taxianalytics.model;

import java.util.Arrays;
import java.util.List;

public final class TaxiTripRecord {
    private final List<String> values;

    private TaxiTripRecord(List<String> values) {
        this.values = List.copyOf(values);
    }

    public static TaxiTripRecord fromValues(String[] values) {
        return new TaxiTripRecord(Arrays.asList(values));
    }

    public String get(int index) {
        return values.get(index);
    }

    public String getVendorId() {
        return get(0);
    }

    public String getPickupDatetime() {
        return get(1);
    }

    public String getDropoffDatetime() {
        return get(2);
    }

    public String getPassengerCount() {
        return get(3);
    }

    public String getTripDistance() {
        return get(4);
    }

    public String getRateCodeId() {
        return get(5);
    }

    public String getPaymentType() {
        return get(9);
    }

    public int size() {
        return values.size();
    }

    public String[] toArray() {
        return values.toArray(new String[0]);
    }

    public String toCsv() {
        return String.join(",", values);
    }
}
