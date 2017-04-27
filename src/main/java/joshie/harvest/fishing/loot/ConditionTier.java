package joshie.harvest.fishing.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.fishing.item.ItemFishingRod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ConditionTier implements LootCondition {
    private final int level;

    public ConditionTier(int level) {
        this.level = level;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)context.getKillerPlayer();
            ItemStack held = player.getHeldItemMainhand();
            if (held.getItem() instanceof ItemFishingRod) {
                return ((ItemFishingRod)held.getItem()).getTier(held).getToolLevel() >= level;
            }
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionTier> {
        public Serializer() {
            super(new ResourceLocation(MODID, "tier"), ConditionTier.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionTier value, @Nonnull JsonSerializationContext context) {
            json.addProperty("level", value.level);
        }

        @Nonnull
        public ConditionTier deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionTier(JsonUtils.getInt(json, "level", 1));
        }
    }
}
