package joshie.harvest.calendar.render;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.*;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

import java.text.NumberFormat;
import java.util.Locale;

import static joshie.harvest.calendar.HFCalendar.HIDE_GOLD_TEXTURE;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFEvents(Side.CLIENT)
public class CalendarHUD {
    private static final ResourceLocation MINE_HUD = new ResourceLocation(MODID, "textures/hud/mine.png");
    public static boolean editingCalendar;
    public static boolean editingGold;

    private String formatTime(int time) {
        int hour = time / 1000;
        int minute = (int) ((double) (time % 1000) / 20 * 1.2);
        if (HFCalendar.CLOCK_24H) {
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
                boolean inMine = mc.theWorld.provider.getDimension() == HFMining.MINING_ID;
                Season season = HFApi.calendar.getSeasonAtCoordinates(MCClientHelper.getWorld(), new BlockPos(MCClientHelper.getPlayer()));
                if (season != null) {
                    SeasonData data = CalendarAPI.INSTANCE.getDataForSeason(season);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                    float adjustedX = ((HFCalendar.X_CALENDAR / 100F) * maxWidth);
                    float adjustedY = ((HFCalendar.Y_CALENDAR / 100F) * maxHeight);
                    if (!HFCalendar.HIDE_CALENDAR_TEXTURE) {
                        mc.renderEngine.bindTexture(inMine ? MINE_HUD : data.getResource());
                        mc.ingameGUI.drawTexturedModalRect(adjustedX - 44, adjustedY - 35, 0, 0, 256, 110);
                    }

                    //Enlarge the Day
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1.4F, 1.4F, 1.4F);
                    String header = inMine ? TextFormatting.GRAY + TextHelper.format("harvestfestival.mine.format", "" + MiningHelper.getFloor(mc.thePlayer.chunkCoordX, (int)mc.thePlayer.posY)) : TextHelper.format("harvestfestival.calendar.date", season.getDisplayName(), (date.getDay() + 1));
                    mc.fontRendererObj.drawStringWithShadow(header, (adjustedX / 1.4F) + 30, (adjustedY / 1.4F) + 7, 0xFFFFFFFF);
                    GlStateManager.popMatrix();

                    //Draw the time
                    GlStateManager.pushMatrix();
                    String time = formatTime(CalendarHelper.getScaledTime((int) CalendarHelper.getTime(MCClientHelper.getWorld())));
                    mc.fontRendererObj.drawStringWithShadow("(" + date.getWeekday().getLocalizedName() + ")" + "  " + time, adjustedX + 42, adjustedY + 23, 0xFFFFFFFF);
                    GlStateManager.popMatrix();
                }
            }

            if (HFCalendar.ENABLE_GOLD_HUD) {
                String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(HFApi.player.getStatsForPlayer(MCClientHelper.getPlayer()).getGold());
                float adjustedX = ((HFCalendar.X_GOLD / 100F) * maxWidth);
                float adjustedY = ((HFCalendar.Y_GOLD / 100F) * maxHeight);
                if (!HIDE_GOLD_TEXTURE) {
                    mc.getTextureManager().bindTexture(HFModInfo.ELEMENTS);
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
