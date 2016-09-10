package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.MCClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class Purchaseable implements IPurchaseable {
    protected ItemStack[] stacks;
    private String resource;
    private long cost;

    public Purchaseable(long cost, ItemStack... stacks) {
        this.cost = cost;
        this.stacks = stacks;
        StringBuilder builder = new StringBuilder();
        for (ItemStack stack: stacks) {
            builder.append(stackToString(stack));
        }

        resource = builder.toString();
    }

    static String stackToString(ItemStack stack) {
        String string = stack.getItem().getRegistryName().toString().replace(":", "_");
        if (stack.getItemDamage() != 0) string = string + "_" + stack.getItemDamage();
        if (stack.getTagCompound() != null) string = string + "_" + stack.getTagCompound().hashCode();
        return string;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return canBuy(world, player);
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public ItemStack getDisplayStack() {
        return stacks[0];
    }

    @Override
    public boolean onPurchased(EntityPlayer player) {
        for (ItemStack product : stacks) {
            SpawnItemHelper.addToPlayerInventory(player, product.copy());
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        for (ItemStack stack : stacks) {
            if (stack != null) list.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
        }
    }

    @Override
    public String getPurchaseableID() {
        return resource;
    }
}