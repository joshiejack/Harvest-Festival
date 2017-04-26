package joshie.harvest.mining.loot;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;

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

        public void serialize(@Nonnull JsonObject json, @Nonnull EndsIn value, @Nonnull JsonSerializationContext context) {
            json.addProperty("in", value.in);
        }

        @Nonnull
        public EndsIn deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new EndsIn(JsonUtils.getInt(json, "in", 0));
        }
    }
}
