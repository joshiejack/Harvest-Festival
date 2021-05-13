package uk.joshiejack.harvestcore.database;

import com.google.common.collect.Maps;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.item.ItemSeeds;
import uk.joshiejack.harvestcore.stage.StageHandler;
import uk.joshiejack.harvestcore.stage.StageHandlerDefault;
import uk.joshiejack.harvestcore.stage.StageHandlerNumbered;
import uk.joshiejack.harvestcore.stage.StageHandlerPatterned;
import uk.joshiejack.harvestcore.ticker.crop.CropTicker;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.ticker.TickerRegistry;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import uk.joshiejack.seasons.data.database.SeasonDataLoader;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class CropLoader {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) { //HIGH
        //Normal Plantable crops
        Map<String, Class<? extends StageHandler>> stageHandlerTypes = new HashMap<>();
        stageHandlerTypes.put("default", StageHandlerDefault.class);
        stageHandlerTypes.put("patterned", StageHandlerPatterned.class);
        stageHandlerTypes.put("numbered", StageHandlerNumbered.class);

        Map<String, StageHandler> stageHandlers = Maps.newHashMap();
        //Load in the growth patterns for the crops
        for (Row row: event.getData("growth_patterns")) {
            String name = row.get("name");
            String type = row.get("type");
            Class<? extends StageHandler> clazz = stageHandlerTypes.get(type);
            try {
                stageHandlers.put(name, clazz.newInstance().createFromData(row));
            } catch (InstantiationException | IllegalAccessException ex) { ex.printStackTrace(); /* ignore */}
        }

        //Assign the growth patterns to the crops
        for (Row row: event.getData("crops")) {
            //Check if the block and item exist before adding
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(row.get("block")));
            if (block != null && block != Blocks.AIR) {
                String name = "crop_" + row.get("name");
                String pattern = row.get("growth_pattern");
                String seasons = row.get("season_handler");
                SeasonHandler seasonHandler = SeasonDataLoader.SEASON_HANDLERS.get(seasons);
                StageHandler<?> stageHandler = stageHandlers.get(pattern);
                CropTicker crop = new CropTicker(name, stageHandler, seasonHandler, row.get("needs_water"));
                TickerRegistry.registerType(name, crop); //Register a new type for this specific crop
                TickerRegistry.registerBlock(block, name);
                int color = row.getColor("color");
                ItemStack seeds = StackHelper.getStackFromString(row.get("seeds"));
                if (!seeds.isEmpty()) ItemSeeds.register(row.get("name"), color, StackHelper.getStackFromString(row.get("seeds")), stageHandler, seasonHandler, row.get("needs_water"));
                else ItemSeeds.register(row.get("name"), color, block, stageHandler, seasonHandler, row.get("needs_water"));
                if (!row.isEmpty("seeds_name")) ItemSeeds.data.get(row.get("name").toString()).setName(row.get("seeds_name").toString());
            }
        }

        event.table("rubbish").rows().forEach(row -> CropTicker.RUBBISH.add(StateAdapter.fromString(row.get("block"))));
    }
}
