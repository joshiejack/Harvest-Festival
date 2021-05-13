package uk.joshiejack.harvestcore.world.storage.loot.conditions.mine;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.harvestcore.HarvestCore;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;

public class ConditionFloorBetween extends AbstractConditionFloor {
    private final int from;
    private final int to;

    public ConditionFloorBetween(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean testFloor(int floor) {
        return floor >= from && floor <= to;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionFloorBetween> {
        public Serializer() {
            super(new ResourceLocation(HarvestCore.MODID, "between_floors"), ConditionFloorBetween.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionFloorBetween value, @Nonnull JsonSerializationContext context) {
            json.addProperty("floor_start", value.from);
            json.addProperty("floor_end", value.to);
        }

        @Nonnull
        public ConditionFloorBetween deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionFloorBetween(JsonUtils.getInt(json, "floor_start", 0), JsonUtils.getInt(json, "floor_end", 0));
        }
    }
}
