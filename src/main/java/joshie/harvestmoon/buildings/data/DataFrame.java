package joshie.harvestmoon.buildings.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DataFrame extends EntityData {
    private ItemStack stack;
    private int rotation;
    private int facing;

    public DataFrame(){}
    public DataFrame(ItemStack stack, int rotation, int facing) {
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

    @Override
    public String getStringFor(Entity e) {
        EntityItemFrame frame = (EntityItemFrame) e;
        return "tempArray.add(new DataFrame(/*TODO INSERT ITEMSTACK */, " + frame.getRotation() + ", " + frame.hangingDirection + "));";
    }
}
