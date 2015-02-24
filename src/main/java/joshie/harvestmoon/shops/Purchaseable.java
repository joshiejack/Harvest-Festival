package joshie.harvestmoon.shops;

import joshie.harvestmoon.api.interfaces.IPurchaseable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Purchaseable implements IPurchaseable {
    private ItemStack[] stacks;
    private int cost;
    
    public Purchaseable(int cost, ItemStack... stacks) {
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
