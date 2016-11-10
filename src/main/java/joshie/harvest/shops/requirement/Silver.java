package joshie.harvest.shops.requirement;

import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

public class Silver extends AbstractRequirement {
    private static final ItemStack item = HFMining.MATERIALS.getStackFromEnum(Material.SILVER);

    private Silver(ItemStack icon, int cost) {
        super(icon, cost);
    }

    public static Silver of(int amount) {
        return new Silver(item, amount);
    }

    @Override
    public boolean isFulfilled(World world, EntityPlayer player, int amount) {
        return InventoryHelper.hasInInventory(player, ITEM_STACK, getIcon(), (cost * amount));
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        InventoryHelper.takeItemsInInventory(player, ITEM_STACK, getIcon(), cost);
    }
}
