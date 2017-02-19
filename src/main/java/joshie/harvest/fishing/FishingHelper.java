package joshie.harvest.fishing;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

@HFEvents
public class FishingHelper {
    static final HashMap<Pair<Season, WaterType>, ResourceLocation> FISHING_LOOT = new HashMap<>();

    public static boolean isWater(World world, BlockPos... positions) {
        for (BlockPos pos: positions) {
            if (world.getBlockState(pos).getBlock() != Blocks.WATER) return false;
        }

        return true;
    }

    enum WaterType {
        OCEAN, LAKE, RIVER, POND
    }

    private static Pair<Season, WaterType> getLocation(World world, BlockPos pos) {
        Season season = HFApi.calendar.getDate(world).getSeason();
        TownData data = TownHelper.getClosestTownToBlockPos(world, pos, false);
        BlockPos position = data.getCoordinatesFor(BuildingLocations.FISHING_POND_PIER);
        WaterType type;
        if (position != null && position.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 6) {
            type = WaterType.POND;
        } else {
            Biome biome = world.getBiome(pos);
            if (BiomeDictionary.isBiomeOfType(biome, Type.OCEAN)) type = WaterType.OCEAN;
            else if (BiomeDictionary.isBiomeOfType(biome, Type.RIVER)) type = WaterType.RIVER;
            else type = WaterType.LAKE;
        }

        return Pair.of(season, type);
    }

    public static ResourceLocation getFishingTable(World world, BlockPos pos) {
        return FISHING_LOOT.get(getLocation(world, pos));
    }
}
