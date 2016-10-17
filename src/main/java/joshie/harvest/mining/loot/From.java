package joshie.harvest.mining.loot;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class From extends FloorCondition {
    private final int from;

    public From(int from) {
        this.from = from;
    }

    @Override
    public boolean testFloor(int floor) {
        return floor >= from;
    }

    public static class Serializer extends LootCondition.Serializer<From> {
        public Serializer() {
            super(new ResourceLocation(MODID, "from"), From.class);
        }

        public void serialize(JsonObject json, From value, JsonSerializationContext context) {
            json.addProperty("from", value.from);
        }

        public From deserialize(JsonObject json, JsonDeserializationContext context) {
            return new From(JsonUtils.getInt(json, "from", 0));
        }
    }
}
