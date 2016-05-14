package joshie.harvest.buildings.placeable.entities;

import joshie.harvest.buildings.placeable.PlaceableHelper;
import joshie.harvest.core.helpers.LootHelper;
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

    public String getItemStack(ItemStack stack) {
        if (stack == null) {
            return "null";
        }

        String name = PlaceableHelper.getBestGuessName(stack);
        return "new ItemStack(" + name + ", 1, " + stack.getItemDamage() + ")";
    }

    @Override
    public String getStringFor(Entity e, BlockPos pos) {
        EntityItemFrame frame = (EntityItemFrame) e;
        return "list.add(new PlaceableItemFrame(" + getItemStack(frame.getDisplayedItem()) + ", " + frame.getRotation() + ", EnumFacing." + frame.facingDirection.name().toUpperCase() +
                ", new BlockPos(" + pos.getX() + ", " + pos.getY() + "," + pos.getZ() + ")));";
    }
}