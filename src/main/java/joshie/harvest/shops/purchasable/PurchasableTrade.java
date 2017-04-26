package joshie.harvest.shops.purchasable;

import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.shops.requirement.RequirementSizeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class PurchasableTrade extends PurchasableMaterials {
    private RequirementSizeable requirement;
    @Nonnull
    private ItemStack purchased;
    @Nonnull
    private ItemStack large;
    @Nonnull
    private ItemStack medium;
    @Nonnull
    private ItemStack small;
    public static int ticker;

    public PurchasableTrade(@Nonnull ItemStack stack, Sizeable sizeable) {
        super(0, stack);
        this.requirement = new RequirementSizeable(this, sizeable);
        this.requirements = new IRequirement[] { requirement };
        this.large = StackHelper.toStack(stack, 3);
        this.medium = StackHelper.toStack(stack, 2);
        this.small = StackHelper.toStack(stack, 1);
    }

    @Override
    @Nonnull
    public ItemStack getDisplayStack() {
        ticker++;

        int num = ticker %1800;
        if (num < 600) return small;
        else if (num < 1200) return medium;
        else return large;
    }

    @Override
    @Nonnull
    protected ItemStack getPurchasedStack() {
        return purchased;
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        purchased = this.stack.copy();
        int amount = requirement.getPurchased(player);
        if (amount != 0) {
            purchased.setCount(amount);
            SpawnItemHelper.addToPlayerInventory(player, purchased);
        }
    }
}
