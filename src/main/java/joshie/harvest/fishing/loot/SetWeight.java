package joshie.harvest.fishing.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.fishing.HFFishing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;
import static joshie.harvest.fishing.item.ItemFish.*;

public class SetWeight extends LootFunction {
    public SetWeight(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer && stack.getItem() == HFFishing.FISH) {
            EntityPlayer player = (EntityPlayer)context.getKillerPlayer();
            ItemStack held = player.getHeldItemMainhand();
            if (held != null && held.getItem() == HFFishing.FISHING_ROD) {
                return applyFishSizeData(rand, held, stack);
            }
        }

        return stack;
    }

    @SuppressWarnings("ConstantConditions")
    private ItemStack applyFishSizeData(Random rand, ItemStack held, ItemStack stack) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        int min = HFFishing.FISHING_ROD.getMinimumFishSize(held);
        int max = HFFishing.FISHING_ROD.getMaximumFishSize(held);

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

        public void serialize(JsonObject object, SetWeight functionClazz, JsonSerializationContext serializationContext) {}

        public SetWeight deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            return new SetWeight(conditionsIn);
        }
    }
}
