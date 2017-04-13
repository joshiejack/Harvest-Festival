package joshie.harvest.shops.purchasable;

import joshie.harvest.api.shops.IPurchaseableMaterials;
import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.shops.requirement.Logs;
import joshie.harvest.shops.requirement.Stone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PurchasableMaterials extends Purchasable implements IPurchaseableMaterials {
    protected IRequirement[] requirements;

    public PurchasableMaterials(IRequirement... requirements) {
        this.requirements = requirements;
    }

    public PurchasableMaterials(long cost, int logs, int stone, ItemStack stack) {
        super(cost, stack);
        if (logs != 0 && stone == 0) requirements = new IRequirement[] { Logs.of(logs) };
        else if (logs == 0 && stone != 0) requirements = new IRequirement[] { Stone.of(stone) };
        else requirements = new IRequirement[] { Logs.of(logs), Stone.of(stone) };
    }

    public PurchasableMaterials(long cost, ItemStack stack, IRequirement... requirements) {
        super(cost, stack);
        this.requirements = requirements;
    }

    @Override
    public IRequirement[] getRequirements() {
        return requirements;
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        for (IRequirement requirement: requirements) {
            if (!requirement.isFulfilled(world, player, amount)) return false;
        }

        return isPurchasable(world, player);
    }

    @Override
    public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack getDisplayStack() {
        return stack;
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        for (IRequirement requirement: requirements) {
            requirement.onPurchased(player);
        }

        super.onPurchased(player);
    }

    public boolean isPurchasable(World world, EntityPlayer player) {
        return true;
    }

    @Override
    public String getDisplayName() {
        return stack != null ? stack.getDisplayName() : getDisplayStack().getDisplayName();
    }
}
