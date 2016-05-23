package joshie.harvest.calendar;

import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.npc.gui.ContainerNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.text.NumberFormat;
import java.util.Locale;

public class CalendarRender {
    private String formatTime(int time) {
        int hour = time / 1000;
        int minute = (int) ((double) (time % 1000) / 20 * 1.2);
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.CROSSHAIRS) {
            if (MCClientHelper.getPlayer().openContainer instanceof ContainerNPC) {
                event.setCanceled(true);
            }
        } else if (event.getType() == ElementType.HOTBAR) {
            Minecraft mc = MCClientHelper.getMinecraft();
            mc.mcProfiler.startSection("calendarHUD");
            Calendar calendar = HFTrackers.getCalendar(MCClientHelper.getWorld());
            ICalendarDate date = calendar.getDate();
            ISeasonData data = calendar.getSeasonData();
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(data.getResource());
            mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 256, 256);

            //Enlarge the Day
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.4F, 1.4F, 1.4F);
            mc.fontRendererObj.drawStringWithShadow(data.getLocalized() + " " + date.getDay(), 30, 7, 0xFFFFFFFF);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            String time = formatTime(CalendarHelper.getScaledTime((int) CalendarHelper.getTime(MCClientHelper.getWorld())));
            mc.fontRendererObj.drawStringWithShadow("(" + date.getWeekday().name().substring(0, 3) + ")" + "  " + time, 42, 23, 0xFFFFFFFF);
            GlStateManager.popMatrix();

            mc.getTextureManager().bindTexture(HFModInfo.elements);
            String text = NumberFormat.getNumberInstance(Locale.US).format(HFTrackers.getClientPlayerTracker().getStats().getGold());
            int width = event.getResolution().getScaledWidth();
            mc.ingameGUI.drawTexturedModalRect(width - mc.fontRendererObj.getStringWidth(text) - 20, 2, 244, 0, 12, 12);
            int coinWidth = width - mc.fontRendererObj.getStringWidth(text) - 5;
            mc.fontRendererObj.drawStringWithShadow(text, coinWidth, 5, 0xFFFFFFFF);

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            mc.mcProfiler.endSection();
        }
    }

    @SubscribeEvent
    public void onFogRender(RenderFogEvent event) {
        if (!event.getState().getMaterial().isLiquid()) {
            Weather weather = HFTrackers.getCalendar(MCClientHelper.getWorld()).getTodaysWeather();
            if (weather == Weather.BLIZZARD) {
                GlStateManager.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.15F);
            } else if (weather == Weather.SNOW) {
                GlStateManager.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.01F);
            }
        }
    }

    @SubscribeEvent
    public void onFogColor(FogColors event) {
        if (!event.getState().getMaterial().isLiquid()) {
            Weather weather = HFTrackers.getCalendar(MCClientHelper.getWorld()).getTodaysWeather();
            if (weather == Weather.SNOW || weather == Weather.BLIZZARD) {
                event.setRed(1F);
                event.setBlue(1F);
                event.setGreen(1F);
            }
        }
    }
}