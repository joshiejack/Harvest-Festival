package uk.joshiejack.harvestcore.database;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.ticker.growable.AbstractGrowableTicker;
import uk.joshiejack.harvestcore.ticker.growable.GrowthTicker;
import uk.joshiejack.harvestcore.ticker.growable.SpreadableTicker;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.ticker.TickerRegistry;
import uk.joshiejack.seasons.data.database.SeasonDataLoader;
import uk.joshiejack.seasons.handlers.SeasonHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class GrowableLoader {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        Map<String, Class<? extends AbstractGrowableTicker>> types = Maps.newHashMap();
        types.put("growth", GrowthTicker.class);
        types.put("spreadable", SpreadableTicker.class);
        Map<String, IBlockState> spreadables = Maps.newHashMap();
        //Assign the growth patterns to the crops
        event.table("growables").rows().forEach(row -> {
            //Check if the block and item exist before adding
            Block theBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(row.get("block").toString().split(" ")[0]));
            if (theBlock != null && theBlock != Blocks.AIR) {
                String name = "growable_" + (row.name().contains(":") ? row.name().split(":")[1] : row.name());
                String type = row.get("type");
                String seasons = row.get("season_handler");
                AbstractGrowableTicker entry = null;
                try {
                    entry = types.get(type).getConstructor(String.class, SeasonHandler.class).newInstance(name, SeasonDataLoader.SEASON_HANDLERS.get(seasons));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {}

                if (entry != null) {
                    TickerRegistry.registerType(name, entry); //Register a new type for this growable
                    theBlock.setTickRandomly(false); //Disable random updates
                    String block = row.get("block");
                    if (block.contains(" ")) {
                        IBlockState state = StateAdapter.fromString(block);
                        TickerRegistry.registerBlockState(state, name);
                        //if (type.equals("spreadable") && row.name().contains(":")) {
                            //new Spreadable(new ResourceLocation(row.name()), state);
                       // }
                    } else {
                        TickerRegistry.registerBlock(Block.REGISTRY.getObject(new ResourceLocation(block)), name);
                    }
                }
            }
        });
    }
}
