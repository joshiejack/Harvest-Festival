package uk.joshiejack.seasons.client.renderer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.SeasonsConfig;
import uk.joshiejack.seasons.client.WorldDataClient;
import uk.joshiejack.seasons.date.CalendarDate;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Seasons.MODID)
public class HUDRenderer {
    public static Int2ObjectMap<HUDRenderData> RENDERERS = new Int2ObjectOpenHashMap<>();
    static {
        RENDERERS.put(0, new SeasonsHUDRender());
    }

    public abstract static class HUDRenderData {
        public abstract ResourceLocation getTexture(Minecraft mc, Season season, CalendarDate date);
        public abstract String getHeader(Minecraft mc, Season season, CalendarDate date);
    }

    private static String formatTime(int time) {
        int hour = time / 1000;
        int minute = (int) ((double) (time % 1000) / 20 * 1.2);
        if (SeasonsConfig.clock24H) {
            return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
        } else {
            boolean pm = false;
            if (hour > 12) {
                hour = hour - 12;
                pm = true;
            }
            if (hour == 12)
                pm = true;
            if (hour == 0)
                hour = 12;

            return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + (pm ? "PM" : "AM");
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            HUDRenderData data = RENDERERS.get(mc.world.provider.getDimension());
            if (data != null) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                CalendarDate date = WorldDataClient.INSTANCE.date;//WorldDataClient.INSTANCE.getDate();
                Season season = WorldDataClient.INSTANCE.getSeason();
                if (date != null && season != null) {
                    int maxWidth = event.getResolution().getScaledWidth();
                    int maxHeight = event.getResolution().getScaledHeight();
                    int x = 0;
                    int y = 0;
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    mc.renderEngine.bindTexture(data.getTexture(mc, season, date));//inMine ? MINE_HUD : season.HUD);
                    mc.ingameGUI.drawTexturedModalRect(x - 44, y - 35, 0, 0, 256, 110);

                    //Enlarge the Day
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1.4F, 1.4F, 1.4F);
                    String header = data.getHeader(mc, season, date);
                    mc.fontRenderer.drawStringWithShadow(header, (x / 1.4F) + 30, (y / 1.4F) + 7, 0xFFFFFFFF);
                    GlStateManager.popMatrix();

                    //Draw the time
                    GlStateManager.pushMatrix();
                    String time = formatTime((int) TimeHelper.getTimeOfDay(mc.world.getWorldTime()));
                    mc.fontRenderer.drawStringWithShadow("(" + TimeHelper.shortName(date.getWeekday()) + ")" + "  " + time, x + 42, y + 23, 0xFFFFFFFF);
                    GlStateManager.popMatrix();
                }

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }
}
