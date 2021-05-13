package uk.joshiejack.seasons.data.database;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.SeasonsConfig;
import uk.joshiejack.seasons.data.SeasonData;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import uk.joshiejack.seasons.handlers.SeasonalCrops;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Locale;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Seasons.MODID)
public class SeasonDataLoader {
    public static final Map<String, SeasonHandler> SEASON_HANDLERS = Maps.newHashMap();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) { //HIGHEST
        //Load in the season data
        event.table("seasons").rows().forEach(row ->
                SeasonData.DATA.put(Season.valueOf(row.get("season").toString().toUpperCase(Locale.ENGLISH)),
                        new SeasonData(TextFormatting.getValueByName(row.get("hud").toString()), row.getAsFloat("temperature"),
                                row.getColor("leaves"), row.getColor("grass"), row.getColor("sky"),
                                row.getAsLong("sunrise"), row.getAsLong("sunset"))));

        //Load in the season handlers
        event.table("season_handlers").rows().forEach(season_handler -> {
            String id = season_handler.id();
            Season season = Season.valueOf(season_handler.get("season").toString().toUpperCase(Locale.ENGLISH));
            if (!SEASON_HANDLERS.containsKey(id)) SEASON_HANDLERS.put(id, new SeasonHandler());
            SEASON_HANDLERS.get(id).getSeasons().add(season); //Add the new seasons
        });

        if (SeasonsConfig.seasonalCropGrowth) {
            event.table("crop_seasons").rows().forEach(row -> SeasonalCrops.REGISTRY.put(Block.REGISTRY.getObject(new ResourceLocation(row.get("block"))),
                    SeasonDataLoader.SEASON_HANDLERS.get(row.get("season_handler").toString())));
        }
    }
}
