package joshie.harvest.shops.requirement;

import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

public class Bricks extends AbstractRequirement {
    private static final ItemStack brickItem = new ItemStack(Items.BRICK);

    private Bricks(ItemStack icon, int cost) {
        super(icon, cost);
    }

    public static Bricks of(int amount) {
        return new Bricks(brickItem, amount);
    }

    @Override
    public boolean isFulfilled(World world, EntityPlayer player, int amount) {
        return InventoryHelper.hasInInventory(player, ORE_DICTIONARY, "ingotBrick", (cost * amount));
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        InventoryHelper.takeItemsInInventory(player, ORE_DICTIONARY, "ingotBrick", cost);
    }
}
