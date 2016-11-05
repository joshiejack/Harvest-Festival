package joshie.harvest.shops.rules;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISpecialPurchaseRules;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SpecialRulesKitchen implements ISpecialPurchaseRules {
    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        return HFApi.player.getRelationsForPlayer(player).getRelationship(HFNPCs.CAFE_GRANNY.getUUID()) >= 15000;
    }
}
