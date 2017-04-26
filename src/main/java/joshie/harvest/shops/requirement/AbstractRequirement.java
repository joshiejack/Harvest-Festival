package joshie.harvest.shops.requirement;

import joshie.harvest.api.shops.IRequirement;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public abstract class AbstractRequirement implements IRequirement {
    @Nonnull
    private final ItemStack icon;
    protected final int cost;

    public AbstractRequirement(@Nonnull ItemStack icon, int cost) {
        this.icon = icon;
        this.cost = cost;
    }

    @Nonnull
    public ItemStack getIcon() {
        return icon;
    }

    public int getCost() {
        return cost;
    }
}