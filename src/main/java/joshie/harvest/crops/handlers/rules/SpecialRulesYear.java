package joshie.harvest.crops.handlers.rules;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SpecialRulesYear implements ISpecialRules {
    private final int years;

    public SpecialRulesYear(int years) {
        this.years = years;
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        return CalendarHelper.getYearsPassed(TownHelper.getClosestTownToEntity(player, false).getBirthday(), HFApi.calendar.getDate(world)) >= years;
    }
}
