package joshie.harvest.fishing.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.town.BuildingLocations;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ConditionLocation extends AbstractCondition {
    private final WaterType location;

    public ConditionLocation(WaterType location) {
        this.location = location;
    }

    @Override
    public boolean testCondition(World world, BlockPos pos) {
        if (location == WaterType.POND) {
            TownData data = TownHelper.getClosestTownToBlockPos(world, pos);
            if (data != null) {
                BlockPos position = data.getCoordinatesFor(BuildingLocations.POND);
                return position != null && position.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 32;
            } else return false;
        } else {
            Biome biome = world.getBiome(pos);
            if (location == WaterType.OCEAN) return BiomeDictionary.isBiomeOfType(biome, Type.OCEAN);
            else if (location == WaterType.RIVER) return BiomeDictionary.isBiomeOfType(biome, Type.RIVER);
            else
                return !BiomeDictionary.isBiomeOfType(biome, Type.OCEAN) && !BiomeDictionary.isBiomeOfType(biome, Type.RIVER);
        }
    }

    public enum WaterType {
        OCEAN, LAKE, RIVER, POND
    }

    public static class Serializer extends LootCondition.Serializer<ConditionLocation> {
        public Serializer() {
            super(new ResourceLocation(MODID, "location"), ConditionLocation.class);
        }

        public void serialize(JsonObject json, ConditionLocation value, JsonSerializationContext context) {
            json.addProperty("type", value.location.name().toLowerCase(Locale.ENGLISH));
        }

        public ConditionLocation deserialize(JsonObject json, JsonDeserializationContext context) {
            return new ConditionLocation(WaterType.valueOf(JsonUtils.getString(json, "type").toUpperCase(Locale.ENGLISH)));
        }
    }
}
