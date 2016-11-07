package joshie.harvest.core.base.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RenderData {
    private static final float MAX_OFFSET1 = 0.5F;
    private static final float MIN_OFFSET1 = -0.5F;
    private static final float MAX_OFFSET2 = 0F / 1.75F;
    private static final float MIN_OFFSET2 = 1F / 1.75F;
    public final float[] rotations = new float[20];
    public final float[] offset1 = new float[20];
    public final float[] offset2 = new float[20];
    public final float[] heightOffset = new float[20];

    public void doRenderUpdate(World world, BlockPos pos, int last) {
        rotations[last] = world.rand.nextFloat() * 360F;
        offset1[last] = 0.5F - world.rand.nextFloat();
        offset2[last] = world.rand.nextFloat() / 1.75F;
        heightOffset[last] = 0.5F + world.rand.nextFloat();
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    private float clampOffset1(float f) {
        return Math.max(MIN_OFFSET1, Math.min(MAX_OFFSET1, f));
    }

    private float clampOffset2(float f) {
        return Math.max(MIN_OFFSET2, Math.min(MAX_OFFSET2, f));
    }

    public void rotate(World world) {
        for (int k = 0; k < rotations.length; k++) {
            rotations[k] = rotations[k] - world.rand.nextInt(7);
        }

        for (int k = 0; k < offset1.length; k++) {
            if (world.rand.nextFloat() < 0.1F) {
                offset1[k] = clampOffset1(offset1[k] + (world.rand.nextBoolean() ? 0.025F : -0.025F));
                offset2[k] = clampOffset2(offset2[k] + (world.rand.nextBoolean() ? 0.025F : -0.025F));
                heightOffset[k] = clampOffset2(heightOffset[k] + (world.rand.nextBoolean() ? world.rand.nextInt(3) : -world.rand.nextInt(3)));
            }
        }
    }
}
