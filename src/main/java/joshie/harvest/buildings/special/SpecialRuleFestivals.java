package joshie.harvest.buildings.special;

import joshie.harvest.api.core.ISpecialPurchaseRules;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SpecialRuleFestivals implements ISpecialPurchaseRules {
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        TownData data = TownHelper.getClosestTownToEntity(player);
        return data.hasBuilding(HFBuildings.SUPERMARKET) || data.hasBuilding(HFBuildings.BARN) || data.hasBuilding(HFBuildings.POULTRY_FARM);
    }
}
