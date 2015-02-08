package joshie.harvestmoon.buildings.placeable.entities;

import joshie.harvestmoon.buildings.placeable.blocks.PlaceableHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PlaceableItemFrame extends PlaceableEntity {
    private ItemStack stack;
    private int rotation;
    private int facing;

    public PlaceableItemFrame() {
        super(0, 0, 0);
    }

    public PlaceableItemFrame(ItemStack stack, int rotation, int facing, int offsetX, int offsetY, int offsetZ) {
        super(offsetX, offsetY, offsetZ);
        this.stack = stack;
        this.rotation = rotation;
        this.facing = facing;
    }

    @Override
    public Entity getEntity(World world, int x, int y, int z) {
        EntityItemFrame frame = new EntityItemFrame(world, x, y, z, facing);
        frame.setDisplayedItem(stack);
        frame.setItemRotation(rotation);
        return frame;
    }

    public String getItemStack(ItemStack stack) {
        if (stack == null) {
            return "null";
        }

        String name = PlaceableHelper.getBestGuessName(stack);
        return "new Itemstack(" + name + ", 1, " + stack.getItemDamage() + ");";
    }

    @Override
    public String getStringFor(Entity e, int x, int y, int z) {
        EntityItemFrame frame = (EntityItemFrame) e;
        return "list.add(new PlaceableItemFrame(" + getItemStack(frame.getDisplayedItem()) + ", " + frame.getRotation() + ", " + frame.hangingDirection + ", " + x + ", " + y + ", " + z + "));";
    }
}
