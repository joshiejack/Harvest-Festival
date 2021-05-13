package uk.joshiejack.piscary.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

import static uk.joshiejack.piscary.Piscary.MODID;

public class ConditionTemp implements LootCondition {
    private final Biome.TempCategory[] categories;

    public ConditionTemp(Biome.TempCategory[] categories) {
        this.categories = categories;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getLootedEntity() != null) {
            World world = context.getLootedEntity().world;
            BlockPos pos = new BlockPos(context.getLootedEntity());
            for (Biome.TempCategory category: categories) {
                if (world.getBiome(pos).getTempCategory() == category) return true;
            }
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionTemp> {
        public Serializer() {
            super(new ResourceLocation(MODID, "biome_temp"), ConditionTemp.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionTemp value, @Nonnull JsonSerializationContext context) {
            if (value.categories.length == 1) {
                json.addProperty("type", value.categories[0].name().toLowerCase(Locale.ENGLISH));
            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < value.categories.length; i++) {
                    Biome.TempCategory type = value.categories[i];
                    builder.append(type.name().toLowerCase(Locale.ENGLISH));
                    if (i < value.categories.length - 1) {
                        builder.append(", ");
                    }
                }

                json.addProperty("types", builder.toString());
            }
        }

        @Nonnull
        public ConditionTemp deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            if (json.has("types")) {
                String value = JsonUtils.getString(json, "types", "warm");
                String[] split = value.replace(" ", "").split(",");
                Biome.TempCategory[] types = new Biome.TempCategory[split.length];
                for (int i = 0; i < split.length; i++) {
                    types[i] = getType(split[i]);
                }

                return new ConditionTemp(types);
            }

            return new ConditionTemp(new Biome.TempCategory[] { getType(JsonUtils.getString(json, "type", "warm")) });
        }

        private Biome.TempCategory getType(String string) {
            return Biome.TempCategory.valueOf(string.toUpperCase());
        }
    }
}
