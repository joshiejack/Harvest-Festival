package uk.joshiejack.harvestcore.world.storage.loot.conditions.mine;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.harvestcore.HarvestCore;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;

public class ConditionFloorFrom extends AbstractConditionFloor {
    private final int from;

    public ConditionFloorFrom(int from) {
        this.from = from;
    }

    @Override
    public boolean testFloor(int floor) {
        return floor >= from;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionFloorFrom> {
        public Serializer() {
            super(new ResourceLocation(HarvestCore.MODID, "from_floor"), ConditionFloorFrom.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionFloorFrom value, @Nonnull JsonSerializationContext context) {
            json.addProperty("floor", value.from);
        }

        @Nonnull
        public ConditionFloorFrom deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionFloorFrom(JsonUtils.getInt(json, "floor", 0));
        }
    }
}
