package uk.joshiejack.penguinlib.util.helpers.generic;

public class MathsHelper {
    public static int constrainToRangeInt(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static double constrainToRangeDouble(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static long constrainToRangeLong(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }

    public static int convertRange(int oldMin, int oldMax, int newMin, double newMax, int value) {
        return (int) ((((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin);
    }
}
