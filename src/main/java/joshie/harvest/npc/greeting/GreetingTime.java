package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.IInfoButton;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

public class GreetingTime implements IInfoButton {
    @Override
    @SuppressWarnings("deprecation")
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        int years = CalendarHelper.getYearsPassed(HFTrackers.getClientPlayerTracker().getStats().getBirthday(), HFTrackers.getCalendar(MCClientHelper.getWorld()).getDate());
        if (years <= 0) return I18n.translateToLocal("harvestfestival.npc.tiberius.first");
        return I18n.translateToLocalFormatted("harvestfestival.npc.tiberius.time", years);
    }
}
