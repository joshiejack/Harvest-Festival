package joshie.harvest.core;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.handlers.SizeableRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Locale;
import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;


public class SetSizeable extends LootFunction {
    private final ResourceLocation resource;
    private final Size size;

    public SetSizeable(LootCondition[] conditionsIn, String sizeable, String size) {
        super(conditionsIn);
        this.resource = new ResourceLocation(MODID, sizeable);
        this.size = Size.valueOf(size.toUpperCase(Locale.ENGLISH));
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        return SizeableRegistry.REGISTRY.getValue(resource).getStack(size);
    }

    public static class Serializer extends LootFunction.Serializer<SetSizeable> {
        protected Serializer() {
            super(new ResourceLocation("hf_set_sizeable"), SetSizeable.class);
        }

        public void serialize(JsonObject object, SetSizeable functionClazz, JsonSerializationContext serializationContext) {
            object.addProperty("resource", functionClazz.resource.toString());
            object.addProperty("size", functionClazz.size.name());
        }

        public SetSizeable deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            return new SetSizeable(conditionsIn, object.get("resource").getAsString(), object.get("size").getAsString());
        }
    }
}
