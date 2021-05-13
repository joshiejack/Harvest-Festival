package uk.joshiejack.economy.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.shipping.ShippingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.Random;

public class RatioValue extends LootFunction {
    private final double ratio;

    public RatioValue(LootCondition[] conditionsIn, double ratio) {
        super(conditionsIn);
        this.ratio = ratio;
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        long value = ShippingRegistry.INSTANCE.getValue(stack);
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setLong("SellValue", (long) (((double) value) * ratio));
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<RatioValue> {
        public Serializer() {
            super(new ResourceLocation(Economy.MODID, "ratio_value"), RatioValue.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull RatioValue functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("ratio", functionClazz.ratio);
        }

        @Nonnull
        public RatioValue deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new RatioValue(conditionsIn, object.get("ratio").getAsDouble());
        }
    }
}
