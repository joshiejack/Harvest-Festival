package uk.joshiejack.gastronomy.client.renderer.tile;

import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRenderData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpecialRenderDataCooking extends SpecialRenderData {
    @Override
    public void doRenderUpdate(World world, BlockPos pos, int last) {
        rotations[last] = world.rand.nextFloat() * 360F;
        offset1[last] = 0.5F - world.rand.nextFloat();
        offset2[last] = world.rand.nextFloat() / 1.75F;
        heightOffset[last] = 0.5F + (last * 0.001F);
        world.markBlockRangeForRenderUpdate(pos, pos);
    }
}
