package joshie.harvest.calendar.render;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.calendar.CalendarAPI;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.calendar.SeasonData;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

import java.text.NumberFormat;
import java.util.Locale;

import static joshie.harvest.calendar.HFCalendar.HIDE_GOLD_TEXTURE;

@HFEvents(Side.CLIENT)
public class CalendarHUD {
    public static boolean editingCalendar;
    public static boolean editingGold;

    private String formatTime(int time) {
        int hour = time / 1000;
        int minute = (int) ((double) (time % 1000) / 20 * 1.2);
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
    }

    private boolean isHUDVisible() {
        return CalendarAPI.INSTANCE.getSeasonProvider(MCClientHelper.getWorld()).displayHUD();
    }

    @SubscribeEvent
    public void keyPress(KeyboardInputEvent.Pre event) {
        if (editingCalendar || editingGold) {
            event.setCanceled(true);
            Keyboard.enableRepeatEvents(true);
            boolean save = false;
            if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
                if (editingCalendar) HFCalendar.HIDE_CALENDAR_TEXTURE = !HFCalendar.HIDE_CALENDAR_TEXTURE; else HFCalendar.HIDE_GOLD_TEXTURE = !HFCalendar.HIDE_GOLD_TEXTURE;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                if (editingCalendar) HFCalendar.Y_CALENDAR--; else HFCalendar.Y_GOLD--;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                if (editingCalendar) HFCalendar.Y_CALENDAR++; else HFCalendar.Y_GOLD++;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_D)  || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                if (editingCalendar) HFCalendar.X_CALENDAR++; else HFCalendar.X_GOLD++;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                if (editingCalendar) HFCalendar.X_CALENDAR--; else HFCalendar.X_GOLD--;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                editingCalendar = false;
                editingGold = false;
                save = true;
            }

            if (HFCalendar.X_CALENDAR >= 80) HFCalendar.X_CALENDAR = 80;
            if (HFCalendar.X_CALENDAR <= -6) HFCalendar.X_CALENDAR = -6;
            if (HFCalendar.Y_CALENDAR >= 90) HFCalendar.Y_CALENDAR = 90;
            if (HFCalendar.Y_CALENDAR <= -2) HFCalendar.Y_CALENDAR = -2;
            if (HFCalendar.X_GOLD >= 0) HFCalendar.X_GOLD = 0;
            if (HFCalendar.X_GOLD <= -90) HFCalendar.X_GOLD = -90;
            if (HFCalendar.Y_GOLD >= 95) HFCalendar.Y_GOLD = 95;
            if (HFCalendar.Y_GOLD <= 0) HFCalendar.Y_GOLD = 0;
            if (save) {
                HFCalendar.save();
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.HOTBAR) {
            Minecraft mc = MCClientHelper.getMinecraft();
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int maxWidth = event.getResolution().getScaledWidth();
            int maxHeight = event.getResolution().getScaledHeight();
            if (HFCalendar.ENABLE_DATE_HUD && isHUDVisible()) {
                Calendar calendar = HFTrackers.getCalendar(MCClientHelper.getWorld());
                CalendarDate date = calendar.getDate();
                SeasonData data = CalendarAPI.INSTANCE.getDataForSeason(HFApi.calendar.getSeasonAtCoordinates(MCClientHelper.getWorld(), new BlockPos(MCClientHelper.getPlayer())));
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                float adjustedX = ((HFCalendar.X_CALENDAR / 100F) * maxWidth);
                float adjustedY = ((HFCalendar.Y_CALENDAR / 100F) * maxHeight);
                if (!HFCalendar.HIDE_CALENDAR_TEXTURE) {
                    mc.renderEngine.bindTexture(data.getResource());
                    mc.ingameGUI.drawTexturedModalRect(adjustedX - 44, adjustedY - 35, 0, 0, 256, 110);
                }

                //Enlarge the Day
                GlStateManager.pushMatrix();
                GlStateManager.scale(1.4F, 1.4F, 1.4F);
                mc.fontRendererObj.drawStringWithShadow(data.getLocalized() + " " + (date.getDay() + 1), (adjustedX / 1.4F) + 30, (adjustedY / 1.4F) + 7, 0xFFFFFFFF);
                GlStateManager.popMatrix();

                //Draw the time
                GlStateManager.pushMatrix();
                String time = formatTime(CalendarHelper.getScaledTime((int) CalendarHelper.getTime(MCClientHelper.getWorld())));
                mc.fontRendererObj.drawStringWithShadow("(" + date.getWeekday().name().substring(0, 3) + ")" + "  " + time, adjustedX + 42, adjustedY + 23, 0xFFFFFFFF);
                GlStateManager.popMatrix();
            }

            if (HFCalendar.ENABLE_GOLD_HUD) {
                String text = NumberFormat.getNumberInstance(Locale.US).format(HFTrackers.getClientPlayerTracker().getStats().getGold());
                float adjustedX = ((HFCalendar.X_GOLD / 100F) * maxWidth);
                float adjustedY = ((HFCalendar.Y_GOLD / 100F) * maxHeight);
                if (!HIDE_GOLD_TEXTURE) {
                    mc.getTextureManager().bindTexture(HFModInfo.elements);
                    mc.ingameGUI.drawTexturedModalRect(maxWidth - mc.fontRendererObj.getStringWidth(text) - 20 + adjustedX, 2 + adjustedY, 244, 0, 12, 12);
                }

                int coinWidth = maxWidth - mc.fontRendererObj.getStringWidth(text) - 5 + (int) adjustedX;
                mc.fontRendererObj.drawStringWithShadow(text, coinWidth, 5 + adjustedY, 0xFFFFFFFF);
            }

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
