package joshie.harvest.shops.requirement;

import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

public class Planks extends AbstractRequirement {
    private static final ItemStack plankItem = new ItemStack(Blocks.PLANKS);

    private Planks(ItemStack icon, int cost) {
        super(icon, cost);
    }

    public static Planks of(int amount) {
        return new Planks(plankItem, amount);
    }

    @Override
    public boolean isFulfilled(World world, EntityPlayer player, int amount) {
        return InventoryHelper.hasInInventory(player, ORE_DICTIONARY, "plankWood", (cost * amount));
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        InventoryHelper.takeItemsInInventory(player, ORE_DICTIONARY, "plankWood", cost);
    }
}
