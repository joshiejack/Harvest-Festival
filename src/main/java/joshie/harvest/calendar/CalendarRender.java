package joshie.harvest.calendar;

import java.text.NumberFormat;
import java.util.Locale;

import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.handlers.DataHelper;
import joshie.harvest.core.helpers.ClientHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.Translate;
import joshie.harvest.npc.gui.ContainerNPC;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CalendarRender {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.type == ElementType.CROSSHAIRS) {
            if (joshie.harvest.core.helpers.generic.MCClientHelper.getPlayer().openContainer instanceof ContainerNPC) {
                event.setCanceled(true);
            }
        } else if (event.type == ElementType.HOTBAR) {
            Minecraft mc = MCClientHelper.getMinecraft();
            mc.mcProfiler.startSection("calendarHUD");
            Calendar calendar = DataHelper.getCalendar();
            ICalendarDate date = calendar.getDate();
            ISeasonData data = calendar.getSeasonData();
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(data.getResource());
            mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 256, 256);

            //Enlarge the Day
            GL11.glPushMatrix();
            GL11.glScalef(1.4F, 1.4F, 1.4F);
            mc.fontRenderer.drawStringWithShadow(data.getLocalized() + " " + date.getDay(), 30, 8, 0xFFFFFFFF);
            GL11.glPopMatrix();

            mc.fontRenderer.drawStringWithShadow(Translate.translate("year") + " " + date.getYear(), 45, 25, 0xFFFFFFFF);
            mc.getTextureManager().bindTexture(HFModInfo.elements);
            String text = NumberFormat.getNumberInstance(Locale.US).format(ClientHelper.getPlayerData().getGold());
            int width = event.resolution.getScaledWidth();
            mc.ingameGUI.drawTexturedModalRect(width - mc.fontRenderer.getStringWidth(text) - 20, 2, 244, 0, 12, 12);
            mc.fontRenderer.drawStringWithShadow(text, width - mc.fontRenderer.getStringWidth(text) - 5, 5, 0xFFFFFFFF);

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
            mc.mcProfiler.endSection();
        }
    }

    @SubscribeEvent
    public void onFogRender(RenderFogEvent event) {
        if (!event.block.getMaterial().isLiquid()) {
            Weather weather = DataHelper.getCalendar().getTodaysWeather();
            if (weather == Weather.BLIZZARD) {
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.15F);
            } else if (weather == Weather.SNOW) {
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.01F);
            }
        }
    }

    @SubscribeEvent
    public void onFogColor(FogColors event) {
        if (!event.block.getMaterial().isLiquid()) {
            Weather weather = DataHelper.getCalendar().getTodaysWeather();
            if (weather == Weather.SNOW || weather == Weather.BLIZZARD) {
                event.red = 1F;
                event.blue = 1F;
                event.green = 1F;
            }
        }
    }
}
