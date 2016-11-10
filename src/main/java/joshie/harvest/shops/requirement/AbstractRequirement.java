package joshie.harvest.shops.requirement;

import joshie.harvest.api.shops.IRequirement;
import net.minecraft.item.ItemStack;

public abstract class AbstractRequirement implements IRequirement {
    private final ItemStack icon;
    protected final int cost;

    public AbstractRequirement(ItemStack icon, int cost) {
        this.icon = icon;
        this.cost = cost;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public int getCost() {
        return cost;
    }
}
