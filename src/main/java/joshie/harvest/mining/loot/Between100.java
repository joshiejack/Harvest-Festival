package joshie.harvest.mining.loot;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class Between100 extends FloorCondition {
    private final int from;
    private final int to;

    public Between100(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean testFloor(int floor) {
        int remainder = floor % 100;
        return remainder == 0 && to == 100 || remainder >= from && remainder <= to;
    }

    public static class Serializer extends LootCondition.Serializer<Between100> {
        public Serializer() {
            super(new ResourceLocation(MODID, "between100"), Between100.class);
        }

        public void serialize(JsonObject json, Between100 value, JsonSerializationContext context) {
            json.addProperty("from", value.from);
            json.addProperty("to", value.to);
        }

        public Between100 deserialize(JsonObject json, JsonDeserializationContext context) {
            return new Between100(JsonUtils.getInt(json, "from", 0), JsonUtils.getInt(json, "to", 0));
        }
    }
}
