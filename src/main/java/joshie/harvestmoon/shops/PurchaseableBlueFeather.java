package joshie.harvestmoon.shops;

import joshie.harvestmoon.api.shops.IPurchaseable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableBlueFeather implements IPurchaseable {
    private ItemStack[] stacks;
    private int cost;
    
    public PurchaseableBlueFeather(int cost, ItemStack... stacks) {
        this.cost = cost;
        this.stacks = stacks;
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return true;
        //return PlayerHelper.isElligibleToMarry(player);
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
