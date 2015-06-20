package joshie.harvest.cooking.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityCookingItem extends EntityItem {
    public boolean isFinishedProduct = false;
    
    public EntityCookingItem(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityCookingItem(World world, double x, double y, double z, ItemStack stack, boolean isFinished) {
        super(world, x, y, z);
        setEntityItemStack(stack);
        lifespan = stack.getItem() == null ? 6000 : stack.getItem().getEntityLifespan(stack, world);
        isFinishedProduct = isFinished;
    }

    public EntityCookingItem(World world) {
        super(world);
    }
}