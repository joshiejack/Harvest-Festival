package joshie.harvest.buildings.special;

import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SpecialRuleBuildings implements ISpecialRules {
    private final int amount;

    public SpecialRuleBuildings(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        return amount == 1 && TownHelper.getClosestTownToEntity(player, false).getBuildings().size() >= this.amount;
    }
}
