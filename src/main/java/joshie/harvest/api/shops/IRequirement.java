package joshie.harvest.api.shops;

import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

public class IRequirement {
    private final ItemStack icon;
    protected final int cost;

    public IRequirement(ItemStack icon, int cost) {
        this.icon = icon;
        this.cost = cost;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public int getCost() {
        return cost;
    }

    public boolean isFulfilled(World world, EntityPlayer player, int amount) {
        return InventoryHelper.hasInInventory(player, ITEM_STACK, icon, amount);
    }

    public void onPurchased(EntityPlayer player) {

    }
}
