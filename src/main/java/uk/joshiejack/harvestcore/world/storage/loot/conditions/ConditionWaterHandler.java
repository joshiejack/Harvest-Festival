package uk.joshiejack.harvestcore.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.water.WaterHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Random;

public class ConditionWaterHandler implements LootCondition {
    private final WaterHandler water;

    public ConditionWaterHandler(WaterHandler water) {
        this.water = water;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        Entity hook = context.getLootedEntity();
        if (hook != null) {
            return water.isType(hook.world, new BlockPos(hook));
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionWaterHandler> {
        public Serializer() {
            super(new ResourceLocation(HarvestCore.MODID, "water"), ConditionWaterHandler.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionWaterHandler value, @Nonnull JsonSerializationContext context) {
            json.addProperty("water", value.water.name());
        }

        @Nonnull
        public ConditionWaterHandler deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionWaterHandler(WaterHandler.REGISTRY.get(json.get("water").getAsString()));
        }
    }
}
