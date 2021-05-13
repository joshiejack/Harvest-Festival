package uk.joshiejack.harvestcore.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.database.QualityEvents;
import uk.joshiejack.harvestcore.registry.Quality;
import uk.joshiejack.penguinlib.item.base.ItemBaseFishingRod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.Random;

public class ModifyQuality extends LootFunction {
    public ModifyQuality(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    private ItemStack apply(EntityPlayer player, EnumHand hand, ItemStack stack) {
        ItemStack held = player.getHeldItem(hand);
        if (held.getItem() instanceof ItemFishingRod) {
            int iQuality = held.getItem() instanceof ItemBaseFishingRod ? ((ItemBaseFishingRod)held.getItem()).getQuality(): 100;
            Quality quality = QualityEvents.getQualityFromScript("getQualityFromNumber", iQuality);
            if (quality != null && quality.modifier() != 1D && QualityEvents.hasQuality(stack.getItem())) {
                if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setString("Quality", quality.getRegistryName().toString());
            }
        }

        return stack;
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
            ItemStack ret = apply(player, EnumHand.OFF_HAND, stack);
            return apply(player, EnumHand.MAIN_HAND, ret);
        } else return stack;
    }

    public static class Serializer extends LootFunction.Serializer<ModifyQuality> {
        public Serializer() {
            super(new ResourceLocation(HarvestCore.MODID, "modify"), ModifyQuality.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull ModifyQuality functionClazz, @Nonnull JsonSerializationContext serializationContext) {}

        @Nonnull
        public ModifyQuality deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new ModifyQuality(conditionsIn);
        }
    }
}
