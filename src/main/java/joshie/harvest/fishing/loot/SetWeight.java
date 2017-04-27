package joshie.harvest.fishing.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemFishingRod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.Random;

import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;
import static joshie.harvest.fishing.item.ItemFish.SIZE;

public class SetWeight extends LootFunction {
    public SetWeight(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer && stack.getItem() == HFFishing.FISH) {
            EntityPlayer player = (EntityPlayer)context.getKillerPlayer();
            ItemStack held = player.getHeldItemMainhand();
            if (held.getItem() instanceof ItemFishingRod) {
                return applyFishSizeData(rand, held, stack);
            }
        }

        return stack;
    }

    @SuppressWarnings("ConstantConditions")
    public static ItemStack applyFishSizeData(Random rand, ItemStack held, ItemStack stack) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        ItemFishingRod rod = (ItemFishingRod) held.getItem();
        int min = rod.getMinimumFishSize(held);
        int max = rod.getMaximumFishSize(held);

        int size;
        if (min == max) size = min;
        else size = min + rand.nextInt(1 + (max - min));
        double length = HFFishing.FISH.getLengthFromSizeOfFish(stack, size);
        stack.getTagCompound().setDouble(SIZE, length);
        stack.getTagCompound().setLong(SELL_VALUE, HFFishing.FISH.getEnumFromStack(stack).getSellValue(length));
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetWeight> {
        public Serializer() {
            super(new ResourceLocation("hf_set_weight"), SetWeight.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull SetWeight functionClazz, @Nonnull JsonSerializationContext serializationContext) {}

        @Nonnull
        public SetWeight deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new SetWeight(conditionsIn);
        }
    }
}
