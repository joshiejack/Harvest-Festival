package joshie.harvest.buildings.special;

import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SpecialRuleChurch implements ISpecialRules<EntityPlayer> {
    @Override
    public boolean canDo(World world, EntityPlayer player, int amount) {
        return amount == 1 && TownHelper.getClosestTownToEntity(player).getInhabitants().size() >= 9;
    }
}
