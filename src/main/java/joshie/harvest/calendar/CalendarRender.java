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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
            mc.fontRendererObj.drawStringWithShadow(data.getLocalized() + " " + (date.getDay() + 1), 30, 7, 0xFFFFFFFF);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            String time = formatTime(CalendarHelper.getScaledTime((int) CalendarHelper.getTime(MCClientHelper.getWorld())));
            mc.fontRendererObj.drawStringWithShadow("(" + date.getWeekday(mc.theWorld).name().substring(0, 3) + ")" + "  " + time, 42, 23, 0xFFFFFFFF);
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

    private static final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

    @SubscribeEvent
    public void onFogRender(RenderFogEvent event) {
        if (!event.getState().getMaterial().isLiquid()) {
            Minecraft mc = MCClientHelper.getMinecraft();
            blockpos$mutableblockpos.setPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            int i1 = mc.gameSettings.fancyGraphics ? 10 : 5;
            int j = MathHelper.floor_double(mc.thePlayer.posY);
            int j2 = mc.theWorld.getPrecipitationHeight(blockpos$mutableblockpos).getY();
            int k2 = j - i1;
            int l2 = j + i1;

            if (k2 < j2) {
                k2 = j2;
            }

            if (l2 < j2) {
                l2 = j2;
            }

            if (k2 != l2) {
                Weather weather = HFTrackers.getCalendar(mc.theWorld).getTodaysWeather();
                if (weather.isSnow()) GlStateManager.setFogEnd(Math.min(event.getFarPlaneDistance(), 192.0F) * 0.5F);
                if (weather == Weather.BLIZZARD) {
                    GlStateManager.setFogStart(-200F);
                } else if (weather == Weather.SNOW) {
                    GlStateManager.setFogStart(0F);
                }
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