package uk.joshiejack.penguinlib.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.penguinlib.data.custom.item.ICustomItemMulti;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.Random;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

public class SetString extends LootFunction {
    private final String name;

    public SetString(LootCondition[] conditionsIn, String name) {
        super(conditionsIn);
        this.name = name;
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        if (stack.getItem() instanceof ICustomItemMulti) {
            return ((ICustomItemMulti)stack.getItem()).getStackFromString(name, stack.getCount());
        } else return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetString> {
        public Serializer() {
            super(new ResourceLocation(MOD_ID, "set_string"), SetString.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull SetString functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("string", functionClazz.name);
        }

        @Nonnull
        public SetString deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new SetString(conditionsIn, object.get("string").getAsString());
        }
    }
}
