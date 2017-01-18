package joshie.harvest.npcs.greeting;

import joshie.harvest.api.npc.IInfoButton;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.lib.HFModInfo.ICONS;

public class GreetingTime implements IInfoButton {
    @Override
    @SuppressWarnings("deprecation")
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, NPC npc) {
        int years = CalendarHelper.getYearsPassed(HFTrackers.getClientPlayerTracker().getStats().getBirthday(), HFTrackers.getCalendar(MCClientHelper.getWorld()).getDate());
        if (years <= 0) return I18n.translateToLocal("harvestfestival.npc.tiberius.first");
        return I18n.translateToLocalFormatted("harvestfestival.npc.tiberius.time", years);
    }

    @Override
    public void drawIcon(GuiScreen gui, int x, int y) {
        gui.mc.renderEngine.bindTexture(ICONS);
        gui.drawTexturedModalRect(x, y, 48, 0, 16, 16);
    }

    @SideOnly(Side.CLIENT)
    public String getTooltip() {
        return "harvestfestival.npc.tooltip.calendar";
    }
}
