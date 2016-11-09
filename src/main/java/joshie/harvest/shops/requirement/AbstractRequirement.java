package joshie.harvest.shops.requirement;

import net.minecraft.item.ItemStack;

public abstract class AbstractRequirement {
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
