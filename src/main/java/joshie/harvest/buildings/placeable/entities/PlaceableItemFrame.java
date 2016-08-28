package joshie.harvest.buildings.placeable.entities;

import joshie.harvest.buildings.LootHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableItemFrame extends PlaceableHanging {
    private ResourceLocation chestType;
    private ItemStack stack;
    private int rotation;

    public PlaceableItemFrame() {}
    public PlaceableItemFrame(ResourceLocation chestType, ItemStack stack, int rotation, EnumFacing facing, int x, int y, int z) {
        super(facing, x, y, z);
        this.chestType = chestType;
        this.stack = stack;
        this.rotation = rotation;
    }

    @Override
    public EntityHanging getEntityHanging(World world, BlockPos pos, EnumFacing facing) {
        EntityItemFrame frame = new EntityItemFrame(world, new BlockPos(pos.getX(), pos.getY(), pos.getZ()), facing);
        ItemStack loot = null;

        if (stack != null) loot = stack.copy();
        if (chestType != null) {
            loot = LootHelper.getStack(world, null, chestType);
        }

        frame.setDisplayedItem(loot);
        frame.setItemRotation(this.rotation);
        return frame;
    }

    @Override
    public PlaceableItemFrame getCopyFromEntity(Entity e, int x, int y, int z) {
        EntityItemFrame frame = (EntityItemFrame) e;
        return new PlaceableItemFrame(null, frame.getDisplayedItem(), frame.getRotation(), frame.facingDirection, x, y, z);
    }
}