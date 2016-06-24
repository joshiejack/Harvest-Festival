package joshie.harvest.mining.loot;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class Between extends FloorCondition {
    private final int from;
    private final int to;

    public Between(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean testFloor(int floor) {
        return floor >= from && floor <= to;
    }

    public static class Serializer extends LootCondition.Serializer<Between> {
        public Serializer() {
            super(new ResourceLocation(MODID, "between"), Between.class);
        }

        public void serialize(JsonObject json, Between value, JsonSerializationContext context) {
            json.addProperty("from", value.from);
            json.addProperty("to", value.to);
        }

        public Between deserialize(JsonObject json, JsonDeserializationContext context) {
            return new Between(JsonUtils.getInt(json, "from", 0), JsonUtils.getInt(json, "to", 0));
        }
    }
}
