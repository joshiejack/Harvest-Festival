package joshie.harvest.festivals;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.ResourceLoader;
import joshie.harvest.town.TownHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static joshie.harvest.buildings.HFBuildings.getGson;

public class BuildingFestival extends Building {
    public BuildingFestival(ResourceLocation resource) {
        super(resource);
    }

    @Override
    public void onBuilt(World world, BlockPos pos, Rotation rotation) {
        getFestivalTemplateFromFestival(Festival.NONE).placeBlocks(world, pos, rotation, null); //Place the default blocks
    }

    @Override
    public void onFestivalChanged(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Rotation rotation, @Nonnull Festival oldFestival, @Nonnull Festival newFestival) {
        if (oldFestival.affectsFestivalGrounds()) getFestivalTemplateFromFestival(oldFestival).removeBlocks(world, pos, rotation); //Remove the old festival
        Quest oldQuest = oldFestival.getQuest(); //Remove the old festival quest
        if (oldQuest != null) TownHelper.getClosestTownToBlockPos(world, pos).getQuests().removeAsCurrent(world, oldQuest);
        if (newFestival.affectsFestivalGrounds()) getFestivalTemplateFromFestival(newFestival).placeBlocks(world, pos, rotation, null); //Place the new blocks
        Quest newQuest = newFestival.getQuest(); //Start the new festivals Quest
        if (newQuest != null) TownHelper.getClosestTownToBlockPos(world, pos).getQuests().startQuest(newQuest, true, null);
    }

    private HFTemplate getFestivalTemplateFromFestival(Festival festival) {
        return (getGson().fromJson(ResourceLoader.getJSONResource(festival.getResource(), "festivals"), HFTemplate.class));
    }
}
