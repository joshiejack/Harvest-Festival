package uk.joshiejack.harvestcore.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.database.QualityEvents;
import uk.joshiejack.harvestcore.registry.Quality;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.Random;

public class SetQuality extends LootFunction {
    private final String quality_string;

    public SetQuality(String quality_string, LootCondition[] conditionsIn) {
        super(conditionsIn);
        this.quality_string = quality_string;
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        Quality quality;
        if (quality_string.equals("random")) {
            quality = QualityEvents.getQualityFromScript("getQualityFromNumber", rand.nextInt(100));
        } else quality = Quality.REGISTRY.get(new ResourceLocation(quality_string));

        if (quality != null && quality.modifier() != 1D && QualityEvents.hasQuality(stack.getItem())) {
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("Quality", quality.getRegistryName().toString());
        }

        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetQuality> {
        public Serializer() {
            super(new ResourceLocation(HarvestCore.MODID, "set_quality"), SetQuality.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull SetQuality functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("quality", functionClazz.quality_string);
        }

        @Nonnull
        public SetQuality deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new SetQuality(object.get("quality").getAsString(), conditionsIn);
        }
    }
}
