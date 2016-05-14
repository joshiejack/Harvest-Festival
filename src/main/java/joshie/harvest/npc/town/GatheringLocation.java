package joshie.harvest.npc.town;

import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GatheringLocation {
    public BlockPos pos;
    public Block block;
    public int meta;

    public GatheringLocation() {}
    public GatheringLocation(IBlockState state, BlockPos pos) {
        this.pos = pos;
        this.block = state.getBlock();
        this.meta = this.block.getMetaFromState(state);
    }

    public void readFromNBT(NBTTagCompound tag) {
        pos = NBTHelper.readBlockPos("Location", tag);
        block = Block.REGISTRY.getObject(new ResourceLocation(tag.getString("Block")));
        meta = tag.getInteger("Meta");
    }

    public void writeToNBT(NBTTagCompound tag) {
        NBTHelper.writeBlockPos("Location", tag, pos);
        tag.setString("Block", Block.REGISTRY.getNameForObject(block).getResourcePath());
        tag.setInteger("Meta", meta);
    }
}