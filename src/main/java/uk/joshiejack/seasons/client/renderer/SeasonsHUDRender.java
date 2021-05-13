package uk.joshiejack.seasons.client.renderer;

import uk.joshiejack.seasons.date.CalendarDate;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.data.SeasonData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;
import java.util.Locale;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class SeasonsHUDRender extends HUDRenderer.HUDRenderData {
    private static final EnumMap<Season, ResourceLocation> HUD = new EnumMap<>(Season.class);
    static {
        registerSeasonHUD(Season.SPRING);
        registerSeasonHUD(Season.SUMMER);
        registerSeasonHUD(Season.AUTUMN);
        registerSeasonHUD(Season.WINTER);
    }

    private static void registerSeasonHUD(Season season) {
        HUD.put(season, GuiElements.getTexture(Seasons.MODID, season.name().toLowerCase(Locale.ENGLISH)));
    }

    @Override
    public ResourceLocation getTexture(Minecraft mc, Season season, CalendarDate date) {
        return HUD.get(season);
    }

    @Override
    public String getHeader(Minecraft mc, Season season, CalendarDate date) {
        SeasonData data = SeasonData.DATA.get(season);
        return (data != null ? data.hud: "") + StringHelper.format(Seasons.MODID + ".hud", season.getName(), date.getDay());
    }
}
