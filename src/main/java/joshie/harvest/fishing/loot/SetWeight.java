package joshie.harvest.fishing.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.fishing.IWeightedItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Locale;
import java.util.Random;

public class SetWeight extends LootFunction {
    public SetWeight(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)context.getKillerPlayer();
            ItemStack held = player.getHeldItemMainhand();
            if (held != null && held.getItem() instanceof IWeightedItem) {
                return ((IWeightedItem)held.getItem()).getInWeightRange(rand, held, stack);
            }
        }

        return stack.getItem() instanceof IWeightedItem ? ((IWeightedItem)stack.getItem()).getInWeightRange(rand, null, stack) : stack;
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
