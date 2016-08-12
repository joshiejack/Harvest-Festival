package joshie.harvest.crops;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.api.HFApi;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;


/**
 * This loot condition checks the biome of the bobber
 **/
public class SetCropType extends LootFunction {
    private final String crop;

    public SetCropType(LootCondition[] conditionsIn, String crop) {
        super(conditionsIn);
        this.crop = crop;
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        return HFApi.crops.getCrop(new ResourceLocation(crop)).getCropStack();
    }

    public static class Serializer extends LootFunction.Serializer<SetCropType> {
        protected Serializer() {
            super(new ResourceLocation("hf_set_crop"), SetCropType.class);
        }

        public void serialize(JsonObject object, SetCropType functionClazz, JsonSerializationContext serializationContext) {
            object.addProperty("crop", functionClazz.crop);
        }

        public SetCropType deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            return new SetCropType(conditionsIn, object.get("crop").getAsString());
        }
    }
}
