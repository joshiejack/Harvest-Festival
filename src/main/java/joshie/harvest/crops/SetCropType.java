package joshie.harvest.crops;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
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
    private static List<Crop> cropsList;
    private final String crop;

    public SetCropType(LootCondition[] conditionsIn, String crop) {
        super(conditionsIn);
        this.crop = crop;
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        if (crop.equals("randomCrop")) return random(true);
        if (crop.equals("randomSeed")) return random(false);
        stack.setItemDamage(CropRegistry.REGISTRY.getId(new ResourceLocation(crop)));
        return stack;
    }

    public ItemStack random(boolean crop) {
        if (cropsList == null) cropsList = new ArrayList<>(CropRegistry.REGISTRY.getValues());
        Collections.shuffle(cropsList);
        return crop ? cropsList.get(0).getCropStack() : cropsList.get(0).getSeedStack();
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
