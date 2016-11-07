package joshie.harvest.fishing.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.fishing.HFFishing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ConditionTier implements LootCondition {
    private final int level;

    public ConditionTier(int level) {
        this.level = level;
    }

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)context.getKillerPlayer();
            ItemStack held = player.getHeldItemMainhand();
            if (held != null && held.getItem() == HFFishing.FISHING_ROD) {
                return HFFishing.FISHING_ROD.getTier(held).getToolLevel() >= level;
            }
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionTier> {
        public Serializer() {
            super(new ResourceLocation(MODID, "tier"), ConditionTier.class);
        }

        public void serialize(JsonObject json, ConditionTier value, JsonSerializationContext context) {
            json.addProperty("level", value.level);
        }

        public ConditionTier deserialize(JsonObject json, JsonDeserializationContext context) {
            return new ConditionTier(JsonUtils.getInt(json, "level", 1));
        }
    }
}
