package joshie.harvest.core;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.base.item.ItemHFEnum;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;


public class SetEnum extends LootFunction {
    private final String name;

    public SetEnum(LootCondition[] conditionsIn, String name) {
        super(conditionsIn);
        this.name = name;
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        if (stack.getItem() instanceof ItemHFEnum) {
            ItemStack ret = ((ItemHFEnum)stack.getItem()).getStackFromEnumString(name);
            ret.stackSize = stack.stackSize; ///Update the size
            return ret;
        } else if (stack.getItem() instanceof ItemBlockHF && ((ItemBlockHF)stack.getItem()).getBlock() instanceof BlockHFEnum) {
            ItemStack ret = ((BlockHFEnum)((ItemBlockHF)stack.getItem()).getBlock()).getStackFromEnumString(name);
            ret.stackSize = stack.stackSize;
            return ret;
        }

        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetEnum> {
        protected Serializer() {
            super(new ResourceLocation("hf_set_enum"), SetEnum.class);
        }

        public void serialize(JsonObject object, SetEnum functionClazz, JsonSerializationContext serializationContext) {
            object.addProperty("enum", functionClazz.name);
        }

        public SetEnum deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            return new SetEnum(conditionsIn, object.get("enum").getAsString());
        }
    }
}
