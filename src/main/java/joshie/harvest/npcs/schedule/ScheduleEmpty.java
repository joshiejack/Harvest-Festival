package joshie.harvest.npcs.schedule;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.ISchedule;
import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static joshie.harvest.town.BuildingLocations.CARPENTER_DOWNSTAIRS;

public class ScheduleEmpty implements ISchedule {
    public static final ScheduleEmpty INSTANCE = new ScheduleEmpty();

    @Nonnull
    @Override
    public BuildingLocation getTarget(World world, EntityLiving entity, NPC npc, @Nullable Season season, Weekday day, long time) {
        return CARPENTER_DOWNSTAIRS;
    }
}
