package joshie.harvest.cooking.blocks;

import joshie.harvest.cooking.Utensil;
import joshie.harvest.cooking.blocks.TileCooking.TileCookingTicking;
import net.minecraft.item.ItemStack;

public class TileMixer extends TileCookingTicking {
    private static final float MAX_OFFSET1 = 0.5F;
    private static final float MIN_OFFSET1 = -0.5F;
    private static final float MAX_OFFSET2 = 0F / 1.75F;
    private static final float MIN_OFFSET2 = 1F / 1.75F;

    public static ItemStack BLADE_STACK;
    public float blade = 0F;

    @Override
    public void animate() {
        for (int k = 0; k < rotations.length; k++) {
            rotations[k] = rotations[k] + 10F;
        }

        for (int k = 0; k < offset1.length; k++) {
            if (worldObj.rand.nextFloat() < 0.1F) {
                offset1[k] = clampOffset1(offset1[k] + (worldObj.rand.nextBoolean() ? 0.05F : -0.05F));
                offset2[k] = clampOffset2(offset2[k] + (worldObj.rand.nextBoolean() ? 0.05F : -0.05F));
            }
        }

        blade += 100F;
    }

    private float clampOffset1(float f) {
        return Math.max(MIN_OFFSET1, Math.min(MAX_OFFSET1, f));
    }

    private float clampOffset2(float f) {
        return Math.max(MIN_OFFSET2, Math.min(MAX_OFFSET2, f));
    }

    @Override
    public Utensil getUtensil() {
        return Utensil.MIXER;
    }

    @Override
    public boolean hasPrerequisites() {
        return isAbove(Utensil.COUNTER);
    }
}