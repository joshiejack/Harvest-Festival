package joshie.harvestmoon.buildings.placeable.entities;

import joshie.harvestmoon.buildings.placeable.PlaceableHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PlaceableItemFrame extends PlaceableHanging {
    private ItemStack stack;
    private int rotation;

    public PlaceableItemFrame() {
        super(0, 0, 0, 0);
    }

    public PlaceableItemFrame(ItemStack stack, int rotation, int facing, int offsetX, int offsetY, int offsetZ) {
        super(facing, offsetX, offsetY, offsetZ);
        this.stack = stack;
        this.rotation = rotation;
    }

    @Override
    public Entity getEntity(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        int facing = getFacing(n1, n2, swap);
        EntityItemFrame frame = new EntityItemFrame(world, getX(x, facing), y, getZ(z, facing), facing);
        frame.setDisplayedItem(stack);
        frame.setItemRotation(rotation);
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
    public String getStringFor(Entity e, int x, int y, int z) {
        EntityItemFrame frame = (EntityItemFrame) e;
        return "list.add(new PlaceableItemFrame(" + getItemStack(frame.getDisplayedItem()) + ", " + frame.getRotation() + ", " + frame.hangingDirection + ", " + x + ", " + y + ", " + z + "));";
    }
}
