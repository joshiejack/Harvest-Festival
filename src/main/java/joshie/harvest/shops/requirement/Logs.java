package joshie.harvest.shops.requirement;

import joshie.harvest.api.shops.IPurchaseableMaterials.Requirement;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

public class Logs extends Requirement {
    private static final ItemStack logItem = new ItemStack(Blocks.LOG);

    private Logs(ItemStack icon, int cost) {
        super(icon, cost);
    }

    public static Logs of(int amount) {
        return new Logs(logItem, amount);
    }

    @Override
    public boolean isFulfilled(World world, EntityPlayer player, int amount) {
        return InventoryHelper.hasInInventory(player, ORE_DICTIONARY, "logWood", (cost * amount));
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        InventoryHelper.takeItemsInInventory(player, ORE_DICTIONARY, "logWood", cost);
    }
}
