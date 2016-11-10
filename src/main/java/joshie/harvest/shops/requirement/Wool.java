package joshie.harvest.shops.requirement;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.core.Size;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.InventoryHelper.SearchType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static joshie.harvest.core.helpers.InventoryHelper.SPECIAL;

public class Wool extends AbstractRequirement {
    private static final ItemStack item = HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.WOOL, Size.MEDIUM);

    private Wool(ItemStack icon, int cost) {
        super(icon, cost);
    }

    public static Wool of(int amount) {
        return new Wool(item, amount);
    }

    @Override
    public boolean isFulfilled(World world, EntityPlayer player, int amount) {
        return InventoryHelper.hasInInventory(player, SPECIAL, SearchType.WOOL, (cost * amount));
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        InventoryHelper.takeItemsInInventory(player, SPECIAL, SearchType.WOOL, cost);
    }
}
