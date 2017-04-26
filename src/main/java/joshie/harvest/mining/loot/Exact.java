package joshie.harvest.mining.loot;
import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class Exact extends FloorCondition {
    private final int[] values;

    public Exact(int[] value) {
        this.values = value;
    }

    @Override
    public boolean testFloor(int floor) {
        for (int value : values) {
            if (value == floor) return true;
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<Exact> {
        public Serializer() {
            super(new ResourceLocation(MODID, "exact"), Exact.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull Exact value, @Nonnull JsonSerializationContext context) {
            if (value.values.length == 1) {
                json.addProperty("value", value.values[0]);
            } else {
                JsonArray array = new JsonArray();
                for (int v : value.values) {
                    array.add(new JsonPrimitive(v));
                }

                json.add("values", array);
            }
        }

        @Nonnull
        public Exact deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            if (json.has("values")) {
                JsonArray array = json.getAsJsonArray("values");
                int[] values = new int[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    values[i] = array.get(i).getAsInt();
                }

                return new Exact(values);
            } else if (json.has("value")) {
                int[] values = new int[1];
                values[0] = JsonUtils.getInt(json, "value", 0);
                return new Exact(values);
            }

            return new Exact(new int[0]);
        }
    }
}