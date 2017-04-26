package joshie.harvest.fishing.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.calendar.CalendarHelper;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ConditionTime implements LootCondition {
    private final int from;
    private final int to;

    public ConditionTime(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        return context.getLootedEntity() != null && CalendarHelper.isBetween(context.getLootedEntity().world, from, to);
    }

    public static class Serializer extends LootCondition.Serializer<ConditionTime> {
        public Serializer() {
            super(new ResourceLocation(MODID, "time"), ConditionTime.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionTime value, @Nonnull JsonSerializationContext context) {
            json.addProperty("from", value.from);
            json.addProperty("to", value.to);
        }

        @Nonnull
        public ConditionTime deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionTime(JsonUtils.getInt(json, "from", 0), JsonUtils.getInt(json, "to", 24000));
        }
    }
}
