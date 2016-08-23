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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class SetCropType extends LootFunction {
    private static final Random rand = new Random();
    private final String crop;

    public SetCropType(LootCondition[] conditionsIn, String crop) {
        super(conditionsIn);
        this.crop = crop;
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        if (crop.equals("random")) return random();
        return HFApi.crops.getCrop(new ResourceLocation(crop)).getCropStack();
    }

    public ItemStack random() {
        List<Crop> cropList = new ArrayList<>(CropRegistry.REGISTRY.getValues());
        Collections.shuffle(cropList);
        return cropList.get(0).getCropStack();
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
