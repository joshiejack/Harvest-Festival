package joshie.harvest.calendar;

import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.NumberFormat;
import java.util.Locale;

import static joshie.harvest.core.config.Calendar.*;

public class CalendarHUD {
    private String formatTime(int time) {
        int hour = time / 1000;
        int minute = (int) ((double) (time % 1000) / 20 * 1.2);
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
    }

    private boolean isClientInPosition() {
        EntityPlayer player = MCClientHelper.getPlayer();
        if (player.worldObj.provider.getDimension() == HUD_DIMENSION) {
            return player.posX >= HUD_XSTART && player.posX <= HUD_XEND && player.posZ >= HUD_ZSTART && player.posZ <= HUD_ZEND;
        }

        return false;
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (!ENABLE_HUD_XZ || isClientInPosition()) {
            if (event.getType() == ElementType.HOTBAR) {
                Minecraft mc = MCClientHelper.getMinecraft();
                mc.mcProfiler.startSection("calendarHUD");
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                if (joshie.harvest.core.config.Calendar.ENABLE_DATE_HUD) {
                    Calendar calendar = HFTrackers.getCalendar(MCClientHelper.getWorld());
                    ICalendarDate date = calendar.getDate();
                    ISeasonData data = calendar.getSeasonData();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    mc.renderEngine.bindTexture(data.getResource());
                    mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 256, 256);

                    //Enlarge the Day
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1.4F, 1.4F, 1.4F);
                    mc.fontRendererObj.drawStringWithShadow(data.getLocalized() + " " + (date.getDay() + 1), 30, 7, 0xFFFFFFFF);
                    GlStateManager.popMatrix();

                    GlStateManager.pushMatrix();
                    String time = formatTime(joshie.harvest.core.helpers.CalendarHelper.getScaledTime((int) joshie.harvest.core.helpers.CalendarHelper.getTime(MCClientHelper.getWorld())));
                    mc.fontRendererObj.drawStringWithShadow("(" + date.getWeekday(mc.theWorld).name().substring(0, 3) + ")" + "  " + time, 42, 23, 0xFFFFFFFF);
                    GlStateManager.popMatrix();
                }

                if (joshie.harvest.core.config.Calendar.ENABLE_GOLD_HUD) {
                    mc.getTextureManager().bindTexture(HFModInfo.elements);
                    String text = NumberFormat.getNumberInstance(Locale.US).format(HFTrackers.getClientPlayerTracker().getStats().getGold());
                    int width = event.getResolution().getScaledWidth();
                    mc.ingameGUI.drawTexturedModalRect(width - mc.fontRendererObj.getStringWidth(text) - 20, 2, 244, 0, 12, 12);
                    int coinWidth = width - mc.fontRendererObj.getStringWidth(text) - 5;
                    mc.fontRendererObj.drawStringWithShadow(text, coinWidth, 5, 0xFFFFFFFF);
                }

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                mc.mcProfiler.endSection();
            }
        }
    }
}
