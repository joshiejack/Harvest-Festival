package joshie.harvest.player.town;

import joshie.harvest.api.WorldLocation;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class GatheringLocation extends WorldLocation {
    public Block block;
    public int meta;

    public GatheringLocation() {}
    public GatheringLocation(Block block, int meta, int dimension, int x, int y, int z) {
        super(dimension, x, y, z);
        this.block = block;
        this.meta = meta;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        block = (Block) Block.blockRegistry.getObject(tag.getString("Block"));
        meta = tag.getInteger("Meta");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("Block", Block.blockRegistry.getNameForObject(block));
        tag.setInteger("Meta", meta);
    }
}
