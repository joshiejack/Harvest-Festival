package joshie.harvestmoon.lib;

import java.util.Random;

public enum SizeableMeta {
    EGG(50, 60, 80), MILK(100, 150, 200), WOOL(100, 400, 500);

    private int small, medium, large;

    private SizeableMeta(int small, int medium, int large) {
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public int getSellValue(Size size) {
        if (size == Size.LARGE) return large;
        else if (size == Size.MEDIUM) return medium;
        else return small;
    }

    public static enum Size {
        SMALL, MEDIUM, LARGE;

        public static Size getRandomSize(Random rand, int quality) {
            int check = 110 - quality;
            if (rand.nextInt(check) == 0) {
                return LARGE;
            } else if (rand.nextInt(check / 2) == 0) {
                return MEDIUM;
            } else return SMALL;
        }
    }
}
