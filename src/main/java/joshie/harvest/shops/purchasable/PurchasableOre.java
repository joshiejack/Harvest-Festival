package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PurchasableOre extends Purchasable {
    public PurchasableOre(long cost, @Nonnull ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
        return HFApi.quests.hasCompleted(Quests.SELL_ORES, player);
    }
}