package uk.joshiejack.penguinlib.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.Random;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

public class ApplyFortune extends LootFunction {
    public ApplyFortune(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        stack.setCount(stack.getCount() + rand.nextInt((int) context.getLuck() + 2));
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<ApplyFortune> {
        public Serializer() {
            super(new ResourceLocation(MOD_ID, "apply_fortune"), ApplyFortune.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull ApplyFortune functionClazz, @Nonnull JsonSerializationContext serializationContext) {}

        @Nonnull
        public ApplyFortune deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new ApplyFortune(conditionsIn);
        }
    }
}
