package joshie.lib.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityFakeItem extends EntityItem {
    public EntityFakeItem(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityFakeItem(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z);
        setEntityItemStack(stack);
        lifespan = stack.getItem() == null ? 6000 : stack.getItem().getEntityLifespan(stack, world);
    }

    public EntityFakeItem(World world) {
        super(world);
    }
}