package joshie.harvest.mining.loot;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class EndsIn extends FloorCondition {
    private final int in;

    public EndsIn(int in) {
        this.in = in;
    }

    @Override
    public boolean testFloor(int floor) {
        return floor % 10 == in;
    }

    public static class Serializer extends LootCondition.Serializer<EndsIn> {
        public Serializer() {
            super(new ResourceLocation(MODID, "ends"), EndsIn.class);
        }

        public void serialize(JsonObject json, EndsIn value, JsonSerializationContext context) {
            json.addProperty("in", value.in);
        }

        public EndsIn deserialize(JsonObject json, JsonDeserializationContext context) {
            return new EndsIn(JsonUtils.getInt(json, "in", 0));
        }
    }
}
