package joshie.harvestmoon.shops;

import joshie.harvestmoon.api.shops.IPurchaseable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Purchaseable implements IPurchaseable {
    private ItemStack[] stacks;
    private long cost;
    
    public Purchaseable(long cost, ItemStack... stacks) {
        this.cost = cost;
        this.stacks = stacks;
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return true;
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public ItemStack[] getProducts() {
        return stacks;
    }
}
