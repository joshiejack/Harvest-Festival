package uk.joshiejack.harvestcore.database;

import com.google.common.collect.Maps;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.item.ItemSeedlings;
import uk.joshiejack.harvestcore.ticker.tree.*;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.ticker.TickerRegistry;
import uk.joshiejack.seasons.data.database.SeasonDataLoader;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class TreeLoader {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        //Trees
        //Load the fruit handlers
        Map<String, FruitData> fruits = Maps.newHashMap();
        for (Row row : event.getData("fruit_handlers")) {
            String name = row.get("name");
            int max = row.get("max");
            boolean[] skip = Database.toBooleanArray(row.get("skip"));
            fruits.put(name, new FruitData(max, skip));
        }

        //Assign the fruits their new ticking types
        for (Row row : event.getData("fruits")) {
            String fruitStates[] = row.get("fruit").toString().replace("\"", "").split(",");
            String modid = fruitStates[0].split(":")[0];
            if (Loader.isModLoaded(modid)) {
                String name = "fruit_" + row.get("name");
                String handler = row.get("fruit_handler");
                String seasons = row.get("season_handler");
                FruitData data = fruits.get(handler);
                TickerRegistry.registerType(name, new FruitTicker(name, SeasonDataLoader.SEASON_HANDLERS.get(seasons), data.max, data.skip));

                for (String string : fruitStates) {
                    IBlockState state = StateAdapter.fromString(string);
                    TickerRegistry.registerBlockState(state, name);
                }
            }
        }

        Map<String, SeasonHandler> seasonsMap = Maps.newHashMap();

        //Register the leaves
        for (Row row : event.getData("leaves")) {
            String leafStates[] = row.get("leaves").toString().replace("\"", "").split(",");
            String modid = leafStates[0].split(":")[0];
            if (Loader.isModLoaded(modid)) {
                String entryName = row.get("name");
                String name = "leaves_" + entryName;
                String seasons = row.get("season_handler");
                SeasonHandler seasonHandler = SeasonDataLoader.SEASON_HANDLERS.get(seasons);
                int days = row.get("days");
                seasonsMap.put(entryName, seasonHandler);
                TickerRegistry.registerType(name, new LeavesTicker(name, seasonHandler, days));
                for (String string : leafStates) {
                    IBlockState state = StateAdapter.fromString(string);
                    TickerRegistry.registerBlockState(state, name);
                }
            }
        }

        //SeedlingTicker > Sapling
        //SaplingTicker > Juvenile
        //JuvenileTicker > Tree
        //Register the trees/saplings
        event.table("trees").rows().forEach(row -> {
            ResourceLocation sapling = new ResourceLocation(row.get("sapling_new").toString());
            if (Loader.isModLoaded(sapling.getNamespace())) {
                String entryName = row.get("name");
                String base_name = "tree_" + entryName;
                IBlockState seedling = StateAdapter.fromString(row.get("seedling"));
                IBlockState sapling_new = StateAdapter.fromString(row.get("sapling_new"));
                IBlockState sapling_old = StateAdapter.fromString(row.get("sapling_old"));
                IBlockState juvenile = StateAdapter.fromString(row.get("juvenile"));
                if (sapling_new != null) {
                    String seedling_name = base_name + "_seedling";
                    String sapling_name = base_name + "_sapling";
                    String juvenile_name = base_name + "_juvenile";
                    TickerRegistry.registerType(seedling_name, new SeedlingTicker(seedling_name, sapling_old, row.get("seedling_days")));
                    TickerRegistry.registerType(sapling_name, new SaplingTicker(seedling_name, sapling_old, juvenile, row.get("sapling_days")));
                    TickerRegistry.registerType(juvenile_name, new JuvenileTicker(seedling_name, sapling_old, row.get("juvenile_days")));
                    TickerRegistry.registerBlockState(seedling, seedling_name);
                    TickerRegistry.registerBlockState(sapling_new, sapling_name);
                    TickerRegistry.registerBlockState(sapling_old, sapling_name);
                    TickerRegistry.registerBlockState(juvenile, juvenile_name);

                    int total_days = row.getAsInt("seedling_days") + row.getAsInt("sapling_days") + row.getAsInt("juvenile_days");
                    //Seedling item register
                    int color = row.getColor("color");
                    ItemStack seeds = new ItemStack(sapling_new.getBlock(), 1, sapling_new.getBlock().getMetaFromState(sapling_new));
                    if (!seeds.isEmpty()) {
                        ItemSeedlings.register(row.get("name"), color, seeds, seedling, total_days, seasonsMap.get(entryName));
                    }
                }
            }
        });
    }

    private static class FruitData {
        int max;
        boolean[] skip;

        FruitData(int max, boolean[] skip) {
            this.max = max;
            this.skip = skip;
        }
    }
}
