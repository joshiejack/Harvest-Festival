package joshie.harvest.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.api.core.Size;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;


public class SetSizeable extends LootFunction {
    private final ISizedProvider provider;
    private final Object object;
    private final Size size;

    @SuppressWarnings("ConstantConditions")
    public SetSizeable(LootCondition[] conditionsIn, String sizeable, String object, String size) {
        super(conditionsIn);
        provider = (ISizedProvider)Item.REGISTRY.getObject(new ResourceLocation(sizeable));
        this.object = provider.getObjectFromString(object);
        this.size = Size.valueOf(size.toUpperCase(Locale.ENGLISH));
    }

    @Override
    @SuppressWarnings("unchecked")
    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        if (stack.getItem() instanceof ISizedProvider) {
            ISizedProvider provider = (ISizedProvider)stack.getItem();
            return provider.getStackOfSize(object, size, 1);
        } else return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetSizeable> {
        public Serializer() {
            super(new ResourceLocation("hf_set_sizeable"), SetSizeable.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull SetSizeable functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("item", ((Item)functionClazz.provider).getRegistryName().toString());
            object.addProperty("object", functionClazz.object.toString());
            object.addProperty("size", functionClazz.size.name());
        }

        @Nonnull
        public SetSizeable deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new SetSizeable(conditionsIn, object.get("item").getAsString(), object.get("object").getAsString(), object.get("size").getAsString());
        }
    }
}
