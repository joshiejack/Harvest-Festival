package joshie.harvest.buildings.special;

import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SpecialRuleFestivals implements ISpecialRules {
    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        TownData data = TownHelper.getClosestTownToEntity(player);
        return amount == 1 && data.hasBuilding(HFBuildings.SUPERMARKET) || data.hasBuilding(HFBuildings.BARN) || data.hasBuilding(HFBuildings.POULTRY_FARM);
    }
}
