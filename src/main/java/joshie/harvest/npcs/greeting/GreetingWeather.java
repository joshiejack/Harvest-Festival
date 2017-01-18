package joshie.harvest.npcs.greeting;

import joshie.harvest.api.npc.IInfoButton;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.calendar.data.CalendarClient;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.ICONS;

public class GreetingWeather implements IInfoButton {
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public String getLocalizedText(EntityPlayer player, EntityAgeable entity, NPC npc) {
        String weather = HFTrackers.<CalendarClient>getCalendar(player.worldObj).getTomorrowsWeather().name().toLowerCase(Locale.ENGLISH);
        return TextHelper.getRandomSpeech(npc, "harvestfestival.npc.goddess.weather." + weather, 32);
    }

    @Override
    public void drawIcon(GuiScreen gui, int x, int y) {
        gui.mc.renderEngine.bindTexture(ICONS);
        gui.drawTexturedModalRect(x, y, 32, 0, 16, 16);
    }

    @SideOnly(Side.CLIENT)
    public String getTooltip() {
        return "harvestfestival.npc.tooltip.weather";
    }
}
