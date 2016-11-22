package joshie.harvest.crops.handlers.rules;

import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SpecialRulesRanch implements ISpecialRules<EntityPlayer> {
    @Override
    public boolean canDo(World world, EntityPlayer player, int amount) {
        return TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.BARN);
    }
}
