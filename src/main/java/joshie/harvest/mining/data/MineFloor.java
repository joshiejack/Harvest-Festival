package joshie.harvest.mining.data;

import static joshie.harvest.core.helpers.generic.MCServerHelper.getWorld;

import joshie.harvest.blocks.HFBlocks;
import net.minecraft.world.World;

public class MineFloor extends MineBlock {
    public MineFloor() {}

    public MineFloor(int dim, int x, int y, int z) {
        super(dim, x, y, z);
    }

    @Override
    public String getType() {
        return "floor";
    }

    @Override
    public void newDay(int level) {
        World world = getWorld(dimension);
        int meta = world.rand.nextInt(13) <= 10 ? 0 : world.rand.nextInt(16);
        world.setBlock(x, y, z, HFBlocks.dirt, meta, 2);
    }
}
