package joshie.harvestmoon.api.core;

import java.util.Random;

import net.minecraft.item.ItemStack;

/** Items that implement this, come in small, medium and large **/
public interface ISizeable {
    public Size getSize(ItemStack stack);
    

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
