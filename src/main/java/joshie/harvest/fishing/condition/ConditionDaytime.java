package joshie.harvest.fishing.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ConditionDaytime extends AbstractCondition {
    private final boolean isDay;

    public ConditionDaytime(boolean isDay) {
        this.isDay = isDay;
    }

    @Override
    public boolean testCondition(World world, BlockPos pos) {
        return world.isDaytime();
    }

    public static class Serializer extends LootCondition.Serializer<ConditionDaytime> {
        public Serializer() {
            super(new ResourceLocation(MODID, "time"), ConditionDaytime.class);
        }

        public void serialize(JsonObject json, ConditionDaytime value, JsonSerializationContext context) {
            json.addProperty("day", value.isDay);
        }

        public ConditionDaytime deserialize(JsonObject json, JsonDeserializationContext context) {
            return new ConditionDaytime(JsonUtils.getBoolean(json, "day", true));
        }
    }
}
