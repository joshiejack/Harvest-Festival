package joshie.harvest.shops.requirement;

import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

public class Materials extends AbstractRequirement {
    private Materials(Material material, int cost) {
        super(HFMining.MATERIALS.getStackFromEnum(material), cost);
    }

    public static Materials of(Material material, int amount) {
        return new Materials(material, amount);
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
