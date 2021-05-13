package uk.joshiejack.harvestcore.world.storage.loot.conditions.mine;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.harvestcore.HarvestCore;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;

public class ConditionFloorBetweenScaled extends AbstractConditionFloor {
    private final int from;
    private final int to;

    public ConditionFloorBetweenScaled(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean testFloor(int floor) {
        int remainder = floor % 100;
        return remainder == 0 && to == 100 || remainder >= from && remainder <= to;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionFloorBetweenScaled> {
        public Serializer() {
            super(new ResourceLocation(HarvestCore.MODID, "between_floors_scaled"), ConditionFloorBetweenScaled.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionFloorBetweenScaled value, @Nonnull JsonSerializationContext context) {
            json.addProperty("from", value.from);
            json.addProperty("to", value.to);
        }

        @Nonnull
        public ConditionFloorBetweenScaled deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionFloorBetweenScaled(JsonUtils.getInt(json, "from", 0), JsonUtils.getInt(json, "to", 0));
        }
    }
}
