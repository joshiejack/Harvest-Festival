package joshie.harvest.mining;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;


public class LootChance {
    public ItemStack stack;
    public double chance;

    public LootChance(ItemStack stack, double chance) {
        this.stack = stack;
        this.chance = chance / 100;
    }

    public boolean canPlayerObtain(EntityPlayer player) {
        return true;
    }
}
