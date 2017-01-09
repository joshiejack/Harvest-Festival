package joshie.harvest.buildings.building;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.core.util.ResourceLoader;
import joshie.harvest.calendar.HolidayRegistry;
import joshie.harvest.core.util.HFTemplate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.buildings.HFBuildings.getGson;

public class BuildingFestival extends Building {
    @Override
    public void newDay(World world, BlockPos pos, Rotation rotation, CalendarDate dToday, CalendarDate dYesterday) {
        ResourceLocation yesterday = HolidayRegistry.INSTANCE.getHoliday(dYesterday);
        ResourceLocation today = HolidayRegistry.INSTANCE.getHoliday(dToday);
        if (yesterday != today) {
            if (yesterday != null) getTemplate(yesterday).removeBlocks(world, pos, rotation);
            if (today != null) getTemplate(today).placeBlocks(world, pos, rotation, null);
        }
    }

    private HFTemplate getTemplate(ResourceLocation resource) {
        return (getGson().fromJson(ResourceLoader.getJSONResource(resource, "festivals"), HFTemplate.class));
    }
}
