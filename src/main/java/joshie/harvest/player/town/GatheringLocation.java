package joshie.harvest.player.town;

import joshie.harvest.api.WorldLocation;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GatheringLocation extends WorldLocation {
    public Block block;
    public int meta;

    public GatheringLocation() {}

    public GatheringLocation(Block block, int meta, int dimension, BlockPos pos) {
        super(dimension, pos);
        this.block = block;
        this.meta = meta;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        block = Block.REGISTRY.getObject(new ResourceLocation(tag.getString("Block")));
        meta = tag.getInteger("Meta");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("Block", Block.REGISTRY.getNameForObject(block).getResourcePath());
        tag.setInteger("Meta", meta);
    }
}