package joshie.harvestmoon.buildings.placeable.blocks;

import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.helpers.generic.StackHelper;
import net.minecraft.block.Block;
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

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.BLOCKS;
    }

    @Override
    public boolean place(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        if (block == Blocks.air && world.getBlock(x, y, z) == Blocks.air) {
            return false;
            
        }
        
        int meta = getMetaData(n1, n2, swap);
        if (meta == 0) {
            return world.setBlock(x, y, z, block);
        } else {
            return world.setBlock(x, y, z, block, meta, 2);
        }
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
}
