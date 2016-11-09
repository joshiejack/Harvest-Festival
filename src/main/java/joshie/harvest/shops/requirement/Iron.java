package joshie.harvest.shops.requirement;

import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

public class Iron extends AbstractRequirement {
    private static final ItemStack ironItem = new ItemStack(Items.IRON_INGOT);

    private Iron(ItemStack icon, int cost) {
        super(icon, cost);
    }

    public static Iron of(int amount) {
        return new Iron(ironItem, amount);
    }

    @Override
    public boolean isFulfilled(World world, EntityPlayer player, int amount) {
        return InventoryHelper.hasInInventory(player, ORE_DICTIONARY, "ingotIron", (cost * amount));
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        InventoryHelper.takeItemsInInventory(player, ORE_DICTIONARY, "ingotIron", cost);
    }
}
