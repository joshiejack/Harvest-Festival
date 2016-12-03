package joshie.harvest.plugins.crafttweaker.wrappers;

import joshie.harvest.api.shops.IPurchasable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class PurchasableWrapper implements IPurchasable {
    public IPurchasable original;
    public long cost;

    public PurchasableWrapper(IPurchasable purchasable, long cost) {
        this.original = purchasable;
        this.cost = cost;
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        return original.canDo(world, player, amount);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return original.canList(world, player);
    }

    @Override
    public int getStock() {
        return original.getStock();
    }

    @Override
    public ItemStack getDisplayStack() {
        return original.getDisplayStack();
    }

    @Override
    public String getDisplayName() {
        return original.getDisplayName();
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        original.onPurchased(player);
    }

    @Override
    public void addTooltip(List<String> list) {
        original.addTooltip(list);
    }

    @Override
    public String getPurchaseableID() {
        return original.getPurchaseableID();
    }
}
