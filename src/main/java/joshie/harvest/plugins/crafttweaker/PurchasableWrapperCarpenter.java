package joshie.harvest.plugins.crafttweaker;

import joshie.harvest.shops.purchasable.PurchasableBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class PurchasableWrapperCarpenter extends PurchasableBuilder {
    public PurchasableBuilder original;

    public PurchasableWrapperCarpenter(PurchasableBuilder purchasable, int wood, int stone, long cost) {
        super(cost, wood, stone, (ItemStack) null);
        this.original = purchasable;
    }

    @Override
    public boolean isPurchaseable(World world, EntityPlayer player) {
        return original.isPurchaseable(world, player);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return original.canList(world, player);
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

    @Override
    public String getName() {
        return original.getName();
    }
}
