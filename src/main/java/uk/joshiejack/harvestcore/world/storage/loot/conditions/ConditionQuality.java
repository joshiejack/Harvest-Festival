package uk.joshiejack.harvestcore.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.penguinlib.item.base.ItemBaseFishingRod;

import javax.annotation.Nonnull;
import java.util.Random;

public class ConditionQuality implements LootCondition {
    private final int quality;

    public ConditionQuality(int quality) {
        this.quality = quality;
    }

    private int getQuality(EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (held.getItem() instanceof ItemBaseFishingRod) {
            return ((ItemBaseFishingRod)held.getItem()).getQuality();
        } else return 0;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
            int quality = getQuality(player, EnumHand.MAIN_HAND);
            if (quality == 0) quality = getQuality(player, EnumHand.OFF_HAND);
            return quality >= this.quality;
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionQuality> {
        public Serializer() {
            super(new ResourceLocation(HarvestCore.MODID, "quality"), ConditionQuality.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionQuality value, @Nonnull JsonSerializationContext context) {
            json.addProperty("quality", value.quality);
        }

        @Nonnull
        public ConditionQuality deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionQuality(JsonUtils.getInt(json, "quality", 1));
        }
    }
}
