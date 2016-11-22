package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchasableOre extends Purchasable {
    public PurchasableOre(long cost, ItemStack stack) {
        super(cost, stack);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return HFApi.quests.hasCompleted(Quests.SELL_ORES, player);
    }
}
