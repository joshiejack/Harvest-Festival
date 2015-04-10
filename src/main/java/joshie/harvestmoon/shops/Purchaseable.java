package joshie.harvestmoon.shops;

import java.util.List;

import joshie.harvestmoon.api.shops.IPurchaseable;
import joshie.harvestmoon.core.helpers.generic.ItemHelper;
import joshie.harvestmoon.core.util.generic.Text;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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

    @Override
    public void addTooltip(List list) {
        for (ItemStack stack: stacks) {
            list.add(Text.WHITE + stack.getDisplayName());
        }
    }
}
