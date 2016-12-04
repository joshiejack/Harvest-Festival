package joshie.harvest.plugins.crafttweaker.wrapper;

import joshie.harvest.api.shops.IPurchasable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class PurchasableWrapper implements IPurchasable {
    public IPurchasable original;
    public long cost;

    public PurchasableWrapper(IPurchasable purchasable, long cost) {
        this.original = purchasable;
        this.cost = cost;
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return original.canList(world, player);
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public ItemStack getDisplayStack() {
        return original.getDisplayStack();
    }

    @Override
    public boolean onPurchased(EntityPlayer player) {
        return original.onPurchased(player);
    }

    @Override
    public void addTooltip(List<String> list) {
        original.addTooltip(list);
    }

    @Override
    public String getPurchaseableID() {
        return original.getPurchaseableID();
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return original.canBuy(world, player);
    }
}
