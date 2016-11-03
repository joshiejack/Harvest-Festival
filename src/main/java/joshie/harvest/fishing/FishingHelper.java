package joshie.harvest.fishing;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.fishing.entity.EntityFishHookHF;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

public class FishingHelper {
    public static final HashMap<Pair<Season, WaterType>, ResourceLocation> LOOT_TABLES = new HashMap<>();
    public enum WaterType {
        OCEAN, LAKE, RIVER, POND
    }

    public static ResourceLocation getLootTable(EntityFishHookHF hook) {
        Season season = HFApi.calendar.getDate(hook.worldObj).getSeason();
        TownData data = TownHelper.getClosestTownToEntity(hook);
        BlockPos pos = new BlockPos(hook);
        BlockPos position = data.getCoordinatesFor(BuildingLocations.POND);
        WaterType type;
        if (position != null && position.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 32) {
            type = WaterType.POND;
        } else {
            Biome biome = hook.worldObj.getBiome(pos);
            if (BiomeDictionary.isBiomeOfType(biome, Type.OCEAN)) type = WaterType.OCEAN;
            else if (BiomeDictionary.isBiomeOfType(biome, Type.RIVER)) type = WaterType.RIVER;
            else type = WaterType.LAKE;
        }

        return LOOT_TABLES.get(Pair.of(season, type));
    }


}
