package joshie.harvest.crops.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.api.crops.Crop;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;


public class SetCropType extends LootFunction {
    private static List<Crop> cropsList;
    private final String crop;

    @SuppressWarnings("WeakerAccess")
    public SetCropType(LootCondition[] conditionsIn, String crop) {
        super(conditionsIn);
        this.crop = crop;
    }

    @Override
    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        if (crop.equals("randomCrop")) return random(true);
        if (crop.equals("randomSeed")) return random(false);
        ResourceLocation resource = crop.contains(":") ? new ResourceLocation(crop) : new ResourceLocation(MODID, crop);
        Crop theCrop = Crop.REGISTRY.get(resource);
        return theCrop.getCropStack(stack.getCount());
    }

    public ItemStack random(boolean crop) {
        if (cropsList == null) {
            cropsList = new ArrayList<>(Crop.REGISTRY.values());
            cropsList.remove(Crop.NULL_CROP);
        }

        Collections.shuffle(cropsList);
        return crop ? cropsList.get(0).getCropStack(1) : cropsList.get(0).getSeedStack(1);
    }

    public static class Serializer extends LootFunction.Serializer<SetCropType> {
        public Serializer() {
            super(new ResourceLocation("hf_set_crop"), SetCropType.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull SetCropType functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("crop", functionClazz.crop);
        }

        @Nonnull
        public SetCropType deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new SetCropType(conditionsIn, object.get("crop").getAsString());
        }
    }
}
