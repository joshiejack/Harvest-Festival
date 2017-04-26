package joshie.harvest.mining.loot;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;

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

        public void serialize(@Nonnull JsonObject json, @Nonnull From value, @Nonnull JsonSerializationContext context) {
            json.addProperty("from", value.from);
        }

        @Nonnull
        public From deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new From(JsonUtils.getInt(json, "from", 0));
        }
    }
}
