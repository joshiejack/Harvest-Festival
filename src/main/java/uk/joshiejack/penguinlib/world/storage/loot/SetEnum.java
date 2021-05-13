package uk.joshiejack.penguinlib.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.penguinlib.util.interfaces.IPenguinMulti;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.Random;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

public class SetEnum extends LootFunction {
    private final String name;

    public SetEnum(LootCondition[] conditionsIn, String name) {
        super(conditionsIn);
        this.name = name;
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        if (stack.getItem() instanceof IPenguinMulti) {
            return ((IPenguinMulti)stack.getItem()).getStackFromEnumString(name, stack.getCount());
        } else return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetEnum> {
        public Serializer() {
            super(new ResourceLocation(MOD_ID, "set_enum"), SetEnum.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull SetEnum functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("enum", functionClazz.name);
        }

        @Nonnull
        public SetEnum deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new SetEnum(conditionsIn, object.get("enum").getAsString());
        }
    }
}
