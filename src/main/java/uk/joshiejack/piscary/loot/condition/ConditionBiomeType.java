package uk.joshiejack.piscary.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

import static uk.joshiejack.piscary.Piscary.MODID;

public class ConditionBiomeType implements LootCondition {
    private final BiomeDictionary.Type[] biomes;
    private final boolean reverse;

    public ConditionBiomeType(BiomeDictionary.Type[] biomes, boolean reverse) {
        this.biomes = biomes;
        this.reverse = reverse;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getLootedEntity() != null) {
            World world = context.getLootedEntity().world;
            BlockPos pos = new BlockPos(context.getLootedEntity());
            if (reverse) {
                for (BiomeDictionary.Type biome: biomes) {
                    if (!BiomeDictionary.hasType(world.getBiome(pos), biome)) return true;
                }
            } else {
                for (BiomeDictionary.Type biome: biomes) {
                    if (BiomeDictionary.hasType(world.getBiome(pos), biome)) return true;
                }
            }
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionBiomeType> {
        public Serializer() {
            super(new ResourceLocation(MODID, "biome_type"), ConditionBiomeType.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionBiomeType value, @Nonnull JsonSerializationContext context) {
            if (value.biomes.length == 1) {
                json.addProperty("type", value.biomes[0].getName().toLowerCase(Locale.ENGLISH));
            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < value.biomes.length; i++) {
                    BiomeDictionary.Type type = value.biomes[i];
                    builder.append(type.getName().toLowerCase(Locale.ENGLISH));
                    if (i < value.biomes.length - 1) {
                        builder.append(", ");
                    }
                }

                json.addProperty("types", builder.toString());
            }

            if (value.reverse) {
                json.addProperty("reverse", true);
            }
        }

        @Nonnull
        public ConditionBiomeType deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            boolean reverse = json.has("reverse") && json.get("reverse").getAsBoolean();
            if (json.has("types")) {
                String value = JsonUtils.getString(json, "type", "plains");
                String[] split = value.replace(" ", "").split(",");
                BiomeDictionary.Type[] types = new BiomeDictionary.Type[split.length];
                for (int i = 0; i < split.length; i++) {
                    types[i] = getType(split[i]);
                }

                return new ConditionBiomeType(types, reverse);
            }

            return new ConditionBiomeType(new BiomeDictionary.Type[] { getType(JsonUtils.getString(json, "type", "plains")) }, reverse);
        }

        private BiomeDictionary.Type getType(String string) {
            return BiomeDictionary.Type.getType(string.toUpperCase());
        }
    }
}
