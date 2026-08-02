package com.proapps.taxianalytics.exception;

public class InvalidJobException extends IllegalArgumentException {
    public InvalidJobException(String message) {
        super(message);
    }
}
