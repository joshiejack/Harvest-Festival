package joshie.harvest.shops.requirement;

import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

public class Saplings extends AbstractRequirement {
    private static final ItemStack saplingItem = new ItemStack(Blocks.SAPLING);

    private Saplings(ItemStack icon, int cost) {
        super(icon, cost);
    }

    public static Saplings of(int amount) {
        return new Saplings(saplingItem, amount);
    }

    @Override
    public boolean isFulfilled(World world, EntityPlayer player, int amount) {
        return InventoryHelper.hasInInventory(player, ORE_DICTIONARY, "treeSapling", (cost * amount));
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        InventoryHelper.takeItemsInInventory(player, ORE_DICTIONARY, "treeSapling", cost);
    }
}
