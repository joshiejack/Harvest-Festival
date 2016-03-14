package joshie.harvest.buildings.placeable.blocks;

import java.util.UUID;

import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.core.helpers.generic.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PlaceableBlock extends Placeable {
    protected Block block;
    protected int meta;

    public PlaceableBlock() {
        super(0, 0, 0);
    }
    
    public PlaceableBlock(int x, int y, int z) {
        super(x, y, z);
    }

    public PlaceableBlock(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(offsetX, offsetY, offsetZ);
        this.block = block;
        this.meta = meta;
    }
    
    public Block getBlock() {
        return block;
    }

    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        return meta;
    }
    
    public IBlockState getBlockState(boolean n1, boolean n2, boolean swap) {
        return block.getStateFromMeta(meta);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.BLOCKS;
    }

    @Override
    public boolean place(UUID uuid, World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        if (block == Blocks.air && world.getBlock(x, y, z) == Blocks.air) {
            return false;
            
        }
        
        int meta = getMetaData(n1, n2, swap);
        world.setBlock(x, y, z, block);
        return world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }

    /** Called by EntityNPCMiner**/
    public void readFromNBT(NBTTagCompound tag) {
        ItemStack stack = StackHelper.getItemStackFromNBT(tag);
        block = Block.getBlockFromItem(stack.getItem());
        meta = stack.getItemDamage();
        offsetX = tag.getInteger("X");
        offsetY = tag.getInteger("Y");
        offsetZ = tag.getInteger("Z");
    }

    public void writeToNBT(NBTTagCompound tag) {
        ItemStack stack = new ItemStack(block, 1, meta);
        StackHelper.writeItemStackToNBT(tag, stack);
        tag.setInteger("X", offsetX);
        tag.setInteger("Y", offsetY);
        tag.setInteger("Z", offsetZ);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if ((!(obj instanceof PlaceableBlock))) return false;
        PlaceableBlock other = (PlaceableBlock) obj;
        if (offsetX != other.offsetX) return false;
        if (offsetY != other.offsetY) return false;
        if (offsetZ != other.offsetZ) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + offsetX;
        result = prime * result + offsetY;
        result = prime * result + offsetZ;
        return result;
    }
}
