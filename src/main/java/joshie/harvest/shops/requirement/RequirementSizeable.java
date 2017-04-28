package joshie.harvest.shops.requirement;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.shops.purchasable.PurchasableTrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

public class RequirementSizeable implements IRequirement {
    private final ItemStack large;
    private final ItemStack medium;
    private final ItemStack small;
    private PurchasableTrade ticker;
    private int amount;

    public RequirementSizeable(PurchasableTrade ticker, Sizeable sizeable) {
        this.ticker = ticker;
        this.large = HFAnimals.ANIMAL_PRODUCT.getStack(sizeable, Size.LARGE);
        this.medium = HFAnimals.ANIMAL_PRODUCT.getStack(sizeable, Size.MEDIUM);
        this.small = HFAnimals.ANIMAL_PRODUCT.getStack(sizeable, Size.SMALL);
    }

    @Override
    public boolean isFulfilled(World world, EntityPlayer player, int amount) {
        return amount == 1 && (InventoryHelper.hasInInventory(player, ITEM_STACK, small, amount) || InventoryHelper.hasInInventory(player, ITEM_STACK, medium, amount) || InventoryHelper.hasInInventory(player, ITEM_STACK, large, amount));

    }

    @Override
    public void onPurchased(EntityPlayer player) {
    }

    @Override
    public int getCost() {
        return 1;
    }

    @Override
    @Nonnull
    public ItemStack getIcon() {
        int num = PurchasableTrade.ticker % 1800;
        if (num < 600) return small;
        else if (num < 1200) return medium;
        else return large;
    }

    public int getPurchased(EntityPlayer player) {
        if (InventoryHelper.hasInInventory(player, ITEM_STACK, small)) {
            InventoryHelper.takeItemsInInventory(player, ITEM_STACK, small);
            return 1;
        } else if (InventoryHelper.hasInInventory(player, ITEM_STACK, medium)) {
            InventoryHelper.takeItemsInInventory(player, ITEM_STACK, medium);
            return 2;
        } else if (InventoryHelper.hasInInventory(player, ITEM_STACK, large)) {
            InventoryHelper.takeItemsInInventory(player, ITEM_STACK, large);
            return 3;
        }

        return 0;
    }
}