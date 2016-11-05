package joshie.harvest.buildings.special;

import joshie.harvest.api.core.ISpecialPurchaseRules;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SpecialRuleChurch implements ISpecialPurchaseRules {
    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        return amount == 1 && TownHelper.getClosestTownToEntity(player).getInhabitants().size() >= 9;
    }
}
