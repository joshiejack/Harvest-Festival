package joshie.harvest.buildings.special;

import joshie.harvest.api.core.ISpecialPurchaseRules;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SpecialRuleTownhall implements ISpecialPurchaseRules {
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return TownHelper.getClosestTownToEntity(player).getBuildings().size() >= 9;
    }
}
