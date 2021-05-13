package uk.joshiejack.seasons.client.renderer;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.client.WorldDataClient;
import uk.joshiejack.seasons.data.SeasonData;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Seasons.MODID, value = Side.CLIENT)
public class SeasonColours {
    private static final Int2IntMap grassToBlend = new Int2IntOpenHashMap();
    private static final Int2IntMap leavesToBlend = new Int2IntOpenHashMap();
    private static final int DEFAULT_RETURN = -423312312;
    static {
        grassToBlend.defaultReturnValue(DEFAULT_RETURN);
        leavesToBlend.defaultReturnValue(DEFAULT_RETURN);
    }

    private static int getBlendedColour(Int2IntMap map, int original, int additional) {
        try {
            int ret = map.get(original);
            if (ret != DEFAULT_RETURN) return ret;
            else {
                int size = 2;
                int r = (original & 0xFF0000) >> 16;
                int g = (original & 0x00FF00) >> 8;
                int b = original & 0x0000FF;
                r += (additional & 0xFF0000) >> 16;
                g += (additional & 0x00FF00) >> 8;
                b += additional & 0x0000FF;

                int value = (r / size & 255) << 16 | (g / size & 255) << 8 | b / size & 255;
                map.put(original, value);
                return value;
            }
        } catch (IndexOutOfBoundsException exception) {
            return original;
        }
    }

    @SubscribeEvent
    public static void getFoliageColor(BiomeEvent.GetFoliageColor event) {
        Season season = WorldDataClient.INSTANCE.fromBiome(event.getBiome());
        SeasonData data = SeasonData.DATA.get(season);
        if (data != null && data.leaves != 0) {
            if (season == Season.AUTUMN) {
                event.setNewColor(data.leaves);
            } else {
                event.setNewColor(getBlendedColour(leavesToBlend, event.getOriginalColor(), data.leaves));
            }
        }
    }

    @SubscribeEvent
    public static void getGrassColor(BiomeEvent.GetGrassColor event) {
        Season season = WorldDataClient.INSTANCE.fromBiome(event.getBiome());
        SeasonData data = SeasonData.DATA.get(season);
        if (data != null && data.grass != 0) {
            event.setNewColor(getBlendedColour(grassToBlend, event.getOriginalColor(), data.grass));
        }
    }
}
