package com.lissajouslaser;

import java.util.Comparator;

/**
 * Implements Comparator interface for comparing string length.
 */
public class OrderStringByLength implements Comparator<String> {

    /**
     * Orders strings based on string length.
     */
    public int compare(String x, String y) {
        return x.length() - y.length();
    }
}
