package uk.joshiejack.seasons.client;

import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.date.CalendarDate;
import uk.joshiejack.seasons.world.storage.AbstractWorldData;
import uk.joshiejack.seasons.world.weather.Weather;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WorldDataClient extends AbstractWorldData {
    public static final WorldDataClient INSTANCE = new WorldDataClient();
    public CalendarDate date;

    @Override
    public void setSeason(Season season) {
        boolean refresh = this.season != season;
        super.setSeason(season);
        if (refresh) Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
