package joshie.harvestmoon.buildings.placeable.blocks;

import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.helpers.generic.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PlaceableBlock extends Placeable {
    private Block block;
    protected int meta;
    private int offsetX;
    private int offsetY;
    private int offsetZ;

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

    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        return meta;
    }

    @Override
    protected boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.BLOCKS;
    }

    @Override
    public void place(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        int meta = getMetaData(n1, n2, swap);
        if (meta == 0) {
            world.setBlock(x, y, z, block);
        } else {
            world.setBlock(x, y, z, block, meta, 2);
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
