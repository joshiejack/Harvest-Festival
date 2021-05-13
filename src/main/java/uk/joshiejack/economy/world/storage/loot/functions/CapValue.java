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

public class CapValue extends LootFunction {
    private final long cap;

    public CapValue(LootCondition[] conditionsIn, long max) {
        super(conditionsIn);
        this.cap = max;
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        long value = ShippingRegistry.INSTANCE.getValue(stack);
        if (value > cap && !stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setLong("SellValue", cap);
        }

        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<CapValue> {
        public Serializer() {
            super(new ResourceLocation(Economy.MODID, "cap_value"), CapValue.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull CapValue functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("cap", functionClazz.cap);
        }

        @Nonnull
        public CapValue deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new CapValue(conditionsIn, object.get("cap").getAsLong());
        }
    }
}
