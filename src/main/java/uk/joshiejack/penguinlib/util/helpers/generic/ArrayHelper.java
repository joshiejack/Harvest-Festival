package uk.joshiejack.penguinlib.util.helpers.generic;

public class ArrayHelper {
    public static <E> E getArrayValue(E[] array, int value) {
        return array[MathsHelper.constrainToRangeInt(value, 0, array.length - 1)];
    }
}
