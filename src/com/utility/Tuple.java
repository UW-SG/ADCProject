package com.utility;

/**
 * Created by Mohit on 10/18/2016.
 */
public class Tuple {
    private final String key;
    private final String value;

    public Tuple(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static Tuple of(String key, String value) {
        return new Tuple(key, value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
