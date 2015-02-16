package joshie.harvestmoon.mining;

import static joshie.harvestmoon.helpers.generic.MCServerHelper.getWorld;

import java.util.ArrayList;

import joshie.harvestmoon.blocks.BlockDirt;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.nbt.NBTTagCompound;
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
        ArrayList<Integer> metas = BlockDirt.getMeta(level);
        int meta = metas.get(world.rand.nextInt(metas.size()));
        world.setBlock(x, y, z, HMBlocks.dirt, meta, 2);
    }
}
