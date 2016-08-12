package io.thesis.collector.dstat.util;

import io.thesis.collector.dstat.DStatCollectorException;


/**
 * Utilities for dstat data conversion.
 * <p>
 * B = byte, k = kb,  M = mb, G = gb
 */
public final class ConversionUtil {

    /**
     * Converts string input data into the number representation.
     *
     * @param rawData raw numerical string input
     * @return the number representation of numerical string input
     */
    public static Double convertKilobytes(final String rawData) {
        final Character lastChar = rawData.charAt(rawData.length() - 1);
        if (Character.isDigit(lastChar)) {
            return Double.valueOf(rawData);
        } else {
            final String numValue = rawData.substring(0, rawData.length() - 1);
            return convertNumber(numValue, lastChar);
        }
    }

    /**
     * Converts string input data in the form '100k' into 100000.
     *
     * @param rawData raw numerical string input
     * @return the number representation of numerical string input
     */
    public static Double convertThousand(final String rawData) {
        final Character lastChar = rawData.charAt(rawData.length() - 1);
        if (Character.isDigit(lastChar)) {
            return Double.valueOf(rawData);
        } else if (String.valueOf(lastChar).equals("k")) {
            return Double.valueOf(rawData.substring(0, rawData.length() - 1)) * 1000;
        } else {
            throw new DStatCollectorException("Unknown unit.");
        }
    }
//
//    /**
//     *
//     *
//     * @param rawData raw numerical string input
//     * @return the number representation of numerical string input
//     */

    /**
     * Converts string input data with a given unit (B,k,M,G) into the number value in Kb.
     *
     * @param numValue the numerical value as string
     * @param unit     the given unit
     * @return converted numerical value
     */
    private static Double convertNumber(final String numValue, final Character unit) {
        final Double inputNumber = Double.valueOf(numValue);
        final String unitString = String.valueOf(unit);
        final Double result;
        switch (unitString) {
            case "B":
                result = inputNumber / 1024;
                break;
            case "k":
                result = inputNumber;
                break;
            case "M":
                result = inputNumber * 1024;
                break;
            case "G":
                result = inputNumber * 1024 * 1024;
                break;
            default:
                throw new DStatCollectorException("Unknown unit: " + unit.toString());
        }
        return result;
    }

    private ConversionUtil() {
    }
}
