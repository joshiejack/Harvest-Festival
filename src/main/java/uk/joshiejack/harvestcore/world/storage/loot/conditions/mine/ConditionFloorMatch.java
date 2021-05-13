package uk.joshiejack.harvestcore.world.storage.loot.conditions.mine;

import com.google.gson.*;
import uk.joshiejack.harvestcore.HarvestCore;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;

public class ConditionFloorMatch extends AbstractConditionFloor {
    private final int[] values;

    public ConditionFloorMatch(int[] value) {
        this.values = value;
    }

    @Override
    public boolean testFloor(int floor) {
        for (int value: values) {
            if (value == floor) return true;
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionFloorMatch> {
        public Serializer() {
            super(new ResourceLocation(HarvestCore.MODID, "floor_match"), ConditionFloorMatch.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionFloorMatch value, @Nonnull JsonSerializationContext context) {
            if (value.values.length == 1) {
                json.addProperty("floor", value.values[0]);
            } else {
                JsonArray array = new JsonArray();
                for (int v : value.values) {
                    array.add(new JsonPrimitive(v));
                }

                json.add("floors", array);
            }
        }

        @Nonnull
        public ConditionFloorMatch deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            if (json.has("floors")) {
                JsonArray array = json.getAsJsonArray("floors");
                int[] values = new int[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    values[i] = array.get(i).getAsInt();
                }

                return new ConditionFloorMatch(values);
            } else if (json.has("floor")) {
                int[] values = new int[1];
                values[0] = JsonUtils.getInt(json, "floor", 0);
                return new ConditionFloorMatch(values);
            }

            return new ConditionFloorMatch(new int[0]);
        }
    }
}
