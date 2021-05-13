package uk.joshiejack.harvestcore.database;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.HCConfig;
import uk.joshiejack.harvestcore.world.gen.VanillaVillageWildernessSpawner;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.loot.LootRegistry;
import uk.joshiejack.seasons.Season;

import java.util.EnumMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class WildernessRegistry {
    public static final EnumMap<Season, LootRegistry<IBlockState>> registries = new EnumMap<>(Season.class);
    public static final Object2ObjectMap<BiomeDictionary.Type, LootRegistry<IBlockState>> biomeRegistry = new Object2ObjectOpenHashMap<>();
    private static final Cache<Pair<Biome, Season>, LootRegistry<IBlockState>> merged = CacheBuilder.newBuilder().build();

    static {
        for (Season season : Season.values()) {
            registries.put(season, new LootRegistry<>());
        }
    }

    public static LootRegistry<IBlockState> getRegistry(Biome biome, Season season) {
        try {
            return merged.get(Pair.of(biome, season), () -> {
                LootRegistry<IBlockState> merge = registries.get(season);
                for (BiomeDictionary.Type type : BiomeDictionary.getTypes(biome)) {
                    if (biomeRegistry.containsKey(type))
                        merge = merge.merge(biomeRegistry.get(type));
                }

                return merge;
            });
        } catch (ExecutionException e) {
            return registries.get(season);
        }
    }

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("wilderness").rows().forEach(row -> {
            Season season = Season.valueOf(row.get("season").toString().toUpperCase(Locale.ENGLISH));
            String modid = row.get("state").toString().split(":")[0];
            if (Loader.isModLoaded(modid)) {
                IBlockState state = StateAdapter.fromString(row.get("state"));
                if (state != null) {
                    double weight = row.getAsDouble("weight");
                    if (state.getBlock() != Blocks.AIR) {
                        registries.get(season).add(state, weight);
                    }
                }
            }
        });

        Object2ObjectMap<String, BiomeDictionary.Type> TYPES = new Object2ObjectOpenHashMap<>();
        event.table("wilderness_biomes").rows().forEach(row -> {
            String modid = row.get("state").toString().split(":")[0];
            if (Loader.isModLoaded(modid)) {
                IBlockState state = StateAdapter.fromString(row.get("state"));
                if (state != null) {
                    String biome = row.get("biome");
                    BiomeDictionary.Type type = TYPES.computeIfAbsent(biome, BiomeDictionary.Type::getType);
                    double weight = row.getAsDouble("weight");
                    if (state.getBlock() != Blocks.AIR) {
                        biomeRegistry.computeIfAbsent(type, (s) -> new LootRegistry<>()).add(state, weight);
                    }
                }
            }
        });

        if (HCConfig.vanillaVillagesGenerateWilderness) {
            MinecraftForge.EVENT_BUS.register(VanillaVillageWildernessSpawner.class);
        }
    }
}
