package uk.joshiejack.economy.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.economy.Economy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.Random;

public class SetValue extends LootFunction {
    private final long minValue;
    private final long maxValue;

    public SetValue(LootCondition[] conditionsIn, long min, long max) {
        super(conditionsIn);
        this.minValue = min;
        this.maxValue = max;
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setLong("SellValue", minValue != maxValue ? context.getWorld().rand.nextInt((int) (maxValue-minValue)) + minValue : minValue);
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetValue> {
        public Serializer() {
            super(new ResourceLocation(Economy.MODID, "set_value"), SetValue.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull SetValue functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            if (functionClazz.minValue == functionClazz.maxValue) object.addProperty("min", functionClazz.minValue);
            else {
                object.addProperty("min", functionClazz.minValue);
                object.addProperty("max", functionClazz.maxValue);
            }
        }

        @Nonnull
        public SetValue deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            if (object.has("min") && object.has("max")) return new SetValue(conditionsIn, object.get("min").getAsLong(), object.get("max").getAsLong());
            else return new SetValue(conditionsIn, object.get("value").getAsLong(), object.get("value").getAsLong());
        }
    }
}
