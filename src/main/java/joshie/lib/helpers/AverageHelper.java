package joshie.lib.helpers;

public class AverageHelper {
    public static int getMode(int[] array) {
        int mode = -1;
        int modefreq = -1;

        for (int current : array) {
            int freq = getNum(array, current);
            if (freq > modefreq) {
                mode = current;
                modefreq = freq;
            }
        }
        return mode;
    }

    private static int getNum(int[] array, int current) {
        int num = 0;
        for (int element : array)
            if (element == current) {
                num++;
            }
        return num;
    }
}
