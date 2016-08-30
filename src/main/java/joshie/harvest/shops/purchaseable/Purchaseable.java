package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.core.helpers.generic.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.WHITE;

public class Purchaseable implements IPurchaseable {
    protected ItemStack[] stacks;
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
            ItemHelper.addToPlayerInventory(player, product.copy());
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        for (ItemStack stack : stacks) {
            if (stack != null) list.add(WHITE + stack.getDisplayName());
        }
    }
}