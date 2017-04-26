package joshie.harvest.mining.loot;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;

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

        public void serialize(@Nonnull JsonObject json, @Nonnull Between100 value, @Nonnull JsonSerializationContext context) {
            json.addProperty("from", value.from);
            json.addProperty("to", value.to);
        }

        @Nonnull
        public Between100 deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new Between100(JsonUtils.getInt(json, "from", 0), JsonUtils.getInt(json, "to", 0));
        }
    }
}
