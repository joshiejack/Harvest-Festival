package joshie.harvest.fishing.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.calendar.CalendarHelper;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

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
    public boolean testCondition(Random rand, LootContext context) {
        return context.getLootedEntity() != null && CalendarHelper.isBetween(context.getLootedEntity().worldObj, from, to);
    }

    public static class Serializer extends LootCondition.Serializer<ConditionTime> {
        public Serializer() {
            super(new ResourceLocation(MODID, "time"), ConditionTime.class);
        }

        public void serialize(JsonObject json, ConditionTime value, JsonSerializationContext context) {
            json.addProperty("from", value.from);
            json.addProperty("to", value.to);
        }

        public ConditionTime deserialize(JsonObject json, JsonDeserializationContext context) {
            return new ConditionTime(JsonUtils.getInt(json, "from", 0), JsonUtils.getInt(json, "to", 24000));
        }
    }
}
