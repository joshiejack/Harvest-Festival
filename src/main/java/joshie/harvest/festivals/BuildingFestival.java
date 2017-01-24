package joshie.harvest.festivals;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.calendar.HolidayRegistry;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.ResourceLoader;
import joshie.harvest.quests.QuestHelper;
import joshie.harvest.town.TownHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.buildings.HFBuildings.getGson;

public class BuildingFestival extends Building {
    public BuildingFestival(ResourceLocation resource) {
        super(resource);
    }

    @Override
    public void newDay(World world, BlockPos pos, Rotation rotation, CalendarDate dToday, CalendarDate dYesterday) {
        Festival yesterday = HolidayRegistry.INSTANCE.getHoliday(dYesterday);
        Festival today = HolidayRegistry.INSTANCE.getHoliday(dToday);
        if (yesterday != today) {
            getTemplate(yesterday).removeBlocks(world, pos, rotation);
            Quest quest = yesterday.getQuest();
            if (quest != null) {
                TownHelper.getClosestTownToBlockPos(world, pos).getQuests().removeAsCurrent(world, quest);
            }

            getTemplate(today).placeBlocks(world, pos, rotation, null);
            quest = today.getQuest();
            if (quest != null) {
                TownHelper.getClosestTownToBlockPos(world, pos).getQuests().startQuest(quest, true, null);
            }
        } else {
            Quest quest = QuestHelper.getQuest("festival." + today.getQuest());
            if (quest != null) {
                if (TownHelper.getClosestTownToBlockPos(world, pos).getQuests().getFinished().contains(quest)) {
                    getTemplate(HolidayRegistry.NONE).placeBlocks(world, pos, rotation, null);
                }
            }
        }
    }

    private HFTemplate getTemplate(Festival festival) {
        return (getGson().fromJson(ResourceLoader.getJSONResource(festival.getResource(), "festivals"), HFTemplate.class));
    }
}
