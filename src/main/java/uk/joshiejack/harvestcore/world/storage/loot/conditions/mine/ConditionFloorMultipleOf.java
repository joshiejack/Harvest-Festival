package uk.joshiejack.harvestcore.world.storage.loot.conditions.mine;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.harvestcore.HarvestCore;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;

public class ConditionFloorMultipleOf extends AbstractConditionFloor {
    private final int multiple;
    private final boolean reverse;

    public ConditionFloorMultipleOf(int multiple, boolean reverse) {
        this.multiple = multiple;
        this.reverse = reverse;
    }

    @Override
    public boolean testFloor(int floor) {
        if (reverse) return floor % multiple != 0;
        return floor % multiple == 0;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionFloorMultipleOf> {
        public Serializer() {
            super(new ResourceLocation(HarvestCore.MODID, "floor_multiple"), ConditionFloorMultipleOf.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionFloorMultipleOf value, @Nonnull JsonSerializationContext context) {
            json.addProperty("of", value.multiple);
            if (value.reverse) {
                json.addProperty("reverse", true);
            }
        }

        @Nonnull
        public ConditionFloorMultipleOf deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionFloorMultipleOf(JsonUtils.getInt(json, "of", 0), JsonUtils.getBoolean(json, "reverse", false));
        }
    }
}
