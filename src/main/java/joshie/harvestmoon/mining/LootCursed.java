package joshie.harvestmoon.mining;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class LootCursed extends LootChance {
    public LootCursed(ItemStack stack, double chance) {
        super(stack, chance);
    }

    //TODO: Can only obtain these once you have a mystril hoe, watering can and sickle
    public boolean canPlayerObtain(EntityPlayer player) {
        return false;
    }
}
