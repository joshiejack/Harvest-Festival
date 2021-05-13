package uk.joshiejack.harvestcore.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.item.HCItems;
import uk.joshiejack.harvestcore.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class SetCrop extends LootFunction {
    private final String crop;

    public SetCrop(String crop, LootCondition[] conditionsIn) {
        super(conditionsIn);
        this.crop = crop;
    }

    @Nonnull
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        if (crop.equals("random")) {
            return HCItems.SEED_BAG.withCrop(stack, ItemSeeds.data.keySet().stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> { Collections.shuffle(collected); return collected.stream(); }))
                    .limit(1)
                    .findAny().get());
        } else return HCItems.SEED_BAG.withCrop(stack, crop);
    }

    public static class Serializer extends LootFunction.Serializer<SetCrop> {
        public Serializer() {
            super(new ResourceLocation(HarvestCore.MODID, "set_crop"), SetCrop.class);
        }

        public void serialize(@Nonnull JsonObject object, @Nonnull SetCrop functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("crop", functionClazz.crop);
        }

        @Nonnull
        public SetCrop deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new SetCrop(object.get("crop").getAsString(), conditionsIn);
        }
    }
}
