package joshie.harvest.buildings.building;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.ResourceLoader;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
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
        TownDataServer town = TownHelper.getClosestTownToBlockPos(world, pos, false);
        removeOldFestival(oldFestival, world, pos, rotation, town);
        addNewFestival(newFestival, world, pos, rotation, town);
    }

    private void removeOldFestival(Festival oldFestival, World world, BlockPos pos, Rotation rotation, TownDataServer town) {
        if (oldFestival.affectsFestivalGrounds()) getFestivalTemplateFromFestival(oldFestival).removeBlocks(world, pos, rotation); //Remove the old festival
        if (oldFestival.getQuest() != null) town.getQuests().removeAsCurrent(world, oldFestival.getQuest());
    }

    private void addNewFestival(Festival newFestival, World world, BlockPos pos, Rotation rotation, TownDataServer town) {
        if (newFestival.affectsFestivalGrounds()) getFestivalTemplateFromFestival(newFestival).placeBlocks(world, pos, rotation, null); //Place the new blocks
        if (newFestival.getQuest() != null) town.getQuests().startQuest(newFestival.getQuest(), true, null);
    }

    private HFTemplate getFestivalTemplateFromFestival(Festival festival) {
        return (getGson().fromJson(ResourceLoader.getJSONResource(festival.getResource(), "festivals"), HFTemplate.class));
    }
}
